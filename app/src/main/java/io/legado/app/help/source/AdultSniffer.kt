package io.legado.app.help.source

/**
 * 主动内容识别：不只看书源名称和分组，而是分析真实抓到的页面内容里的特征词。
 *
 * 命中后给书源加上 [ADULT_GROUP] 分组，书源管理里的「成人」分类会自动把它收进去，
 * 也就能按分类单独筛选、导出、删除。
 */
object AdultSniffer {

    /** 检测命中后写入的分组名（用「成人内容」避免和用户自己建的「成人」分组冲突） */
    const val ADULT_GROUP = "成人内容"

    /** 出现一次基本就能说明问题 */
    private val strongMarkers = listOf(
        "成人小说", "成人漫画", "成人文学", "成人网站", "成人视频", "成人内容",
        "成人专区", "成人限定", "成人阅读", "成人频道",
        "色情小说", "色情漫画", "色情文学", "色情网站", "情色文学", "情色小说",
        "黄色小说", "小黄文", "黄文", "黄书", "肉文", "np文", "h漫", "里番",
        "无码", "有码", "porn", "18禁", "十八禁", "av资源", "av在线",
        "🔞", "po18", "腐小说", "腐文", "污书"
    )

    /** 弱特征：至少命中两条才判定，避免广告或偶然提及误伤 */
    private val weakMarkers = listOf(
        "成人", "色情", "情色", "r18", "未成年禁止", "未满18", "本子", "大尺度", "限制级"
    )

    /**
     * @param rawSample 页面原文（HTML 或 JSON 都行，内部会先去掉标签）
     * @param extraKeywords 自己补充的关键词，英文逗号/中文逗号/分号/换行分隔
     */
    fun hits(rawSample: String, extraKeywords: String = ""): List<String> {
        if (rawSample.isBlank()) return emptyList()
        val cleaned = rawSample
            .replace(Regex("<[^>]*>"), " ")
            .replace(Regex("&[a-zA-Z#0-9]+;"), " ")
            .replace(Regex("\\s+"), " ")
            .lowercase()
        if (cleaned.isBlank()) return emptyList()
        val strong = strongMarkers.filter { cleaned.contains(it) }
        val custom = extraKeywords
            .split(Regex("[,，;；\n\r]+"))
            .map { it.trim().lowercase() }
            .filter { it.isNotEmpty() && cleaned.contains(it) }
            .distinct()
        if (strong.isNotEmpty() || custom.isNotEmpty()) {
            return (strong + custom).distinct().take(3)
        }
        val weak = weakMarkers.filter { cleaned.contains(it) }
        return if (weak.size >= 2) weak.take(3) else emptyList()
    }
}
