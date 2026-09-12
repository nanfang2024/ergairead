package io.legado.app.ui.book.source.manage

import io.legado.app.constant.AppPattern
import io.legado.app.constant.BookSourceType
import io.legado.app.data.entities.BookSource
import io.legado.app.data.entities.BookSourcePart
import io.legado.app.help.source.AdultSniffer
import io.legado.app.utils.splitNotBlank

/**
 * 书源分类。
 *
 * 规则：书源**刚导入时统一是「未分类」**，跑一次书源检测之后才会写入真实分类
 * （小说 / 漫画 / 成人 / 音频 / 视频 / 文件 / 其他）。搜书页、书源管理页都只认这套分类，
 * 不再使用书源自带的分组。
 *
 * 分类存在 `bookSourceGroup` 里（一个分类词 + 若干状态标记，逗号分隔），
 * 这样导出书源时分类跟着走，导入别的书源时又会被重置成「未分类」。
 */
object BookSourceCategory {

    const val UNCLASSIFIED = "未分类"
    const val NOVEL = "小说"
    const val COMIC = "漫画"
    const val ADULT = "成人"
    const val AUDIO = "音频"
    const val VIDEO = "视频"
    const val FILE = "文件"
    const val OTHER = "其他"

    /** 检测之后才会出现的分类 */
    val detected = listOf(NOVEL, COMIC, ADULT, AUDIO, VIDEO, FILE, OTHER)

    /** 界面里展示的全部分类（含未分类） */
    val all = listOf(UNCLASSIFIED) + detected

    private val categoryTokens = all.toSet()

    fun isCategory(token: String): Boolean = token.trim() in categoryTokens

    /** 书源当前分类，没有分类标记就是「未分类」 */
    fun categoryOf(source: BookSourcePart): String = categoryOf(source.bookSourceGroup)

    fun categoryOf(source: BookSource): String = categoryOf(source.bookSourceGroup)

    fun categoryOf(groups: String?): String {
        val tokens = groups?.splitNotBlank(AppPattern.splitGroupRegex).orEmpty()
        return tokens.firstOrNull { isCategory(it) } ?: UNCLASSIFIED
    }

    /** 写入分类：替换旧的分类标记，保留失效、成人内容这类状态标记 */
    fun applyCategory(source: BookSource, category: String) {
        val tokens = source.bookSourceGroup
            ?.splitNotBlank(AppPattern.splitGroupRegex)
            ?.filterNot { isCategory(it) }
            .orEmpty()
        source.bookSourceGroup = (listOf(category) + tokens).joinToString(",")
    }

    /** 把一条书源的分组整理成「分类 + 状态标记」，丢掉外来的自定义分组 */
    fun normalizeGroups(groups: String?): String {
        val tokens = groups?.splitNotBlank(AppPattern.splitGroupRegex).orEmpty()
        val category = tokens.firstOrNull { isCategory(it) } ?: UNCLASSIFIED
        val status = tokens.filter { !isCategory(it) && isStatusMark(it) }
        return (listOf(category) + status).joinToString(",")
    }

    /** 检测时判定分类：主动识别到成人优先，其次看名称、地址和书源类型 */
    fun detect(source: BookSource): String {
        val groups = source.bookSourceGroup?.splitNotBlank(AppPattern.splitGroupRegex).orEmpty()
        if (groups.any { it == AdultSniffer.ADULT_GROUP }) return ADULT
        return classify(source.bookSourceName, source.bookSourceUrl, source.bookSourceType)
    }

    private val adultKeys = listOf(
        "成人", "18+", "18禁", "18x", "r18", "nsfw", "里番", "福利", "激情",
        "色情", "情色", "黄漫", "黄色", "禁书", "肉文", "午夜", "约炮", "福利姬"
    )
    private val comicKeys = listOf("漫画", "漫畫", "看漫", "comic", "manga", "manhua", "动漫")
    private val audioKeys = listOf("听书", "有声", "音频", "audio", "广播剧", "评书", "相声", "听说")
    private val videoKeys = listOf("视频", "video", "短剧", "影视")

    private fun classify(name: String, url: String, type: Int): String {
        val text = "$name $url".lowercase()
        if (adultKeys.any { text.contains(it) }) return ADULT
        return when (type) {
            BookSourceType.audio -> AUDIO
            BookSourceType.image -> COMIC
            BookSourceType.file -> FILE
            BookSourceType.video -> VIDEO
            else -> when {
                comicKeys.any { text.contains(it) } -> COMIC
                audioKeys.any { text.contains(it) } -> AUDIO
                videoKeys.any { text.contains(it) } -> VIDEO
                type == BookSourceType.default -> NOVEL
                else -> OTHER
            }
        }
    }

    /** 校验 / 识别过程写入的状态标记，清理分组时保留 */
    private val statusKeys = listOf("失效", "校验超时", "规则为空", AdultSniffer.ADULT_GROUP, "js失效")

    private fun isStatusMark(token: String): Boolean = statusKeys.any { it in token }

    /** 书源是否被校验标记为不可用（搜索失效 / 发现失效 / 域名失效 / 校验超时…） */
    fun isInvalid(source: BookSourcePart): Boolean = invalidGroups(source).isNotEmpty()

    fun invalidGroups(source: BookSourcePart): List<String> {
        val groups = source.bookSourceGroup
            ?.splitNotBlank(AppPattern.splitGroupRegex)
            ?.toList()
            .orEmpty()
        return groups.filter { group -> group.contains("失效") || group == "校验超时" }
    }
}
