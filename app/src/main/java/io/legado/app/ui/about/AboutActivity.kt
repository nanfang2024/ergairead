package io.legado.app.ui.about

import android.os.Bundle
import io.legado.app.R
import io.legado.app.base.BaseActivity
import io.legado.app.constant.AppConst.appInfo
import io.legado.app.databinding.ActivityAboutBinding
import io.legado.app.lib.theme.UiCorner
import io.legado.app.lib.theme.themeCardColorOrDefault
import io.legado.app.utils.viewbindingdelegate.viewBinding

/**
 * 私有版「关于」页：只显示软件名与版本号。
 * 作者、维护仓库、开源地址、下载地址、公众号等一律不展示。
 */
class AboutActivity : BaseActivity<ActivityAboutBinding>() {

    override val binding by viewBinding(ActivityAboutBinding::inflate)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        binding.llAbout.background = UiCorner.opaqueRounded(
            themeCardColorOrDefault(),
            UiCorner.panelRadius(this)
        )
        binding.tvAppSummary.text = "${getString(R.string.version)} ${appInfo.versionName}"
        val fTag = "aboutFragment"
        var aboutFragment = supportFragmentManager.findFragmentByTag(fTag)
        if (aboutFragment == null) aboutFragment = AboutFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_fragment, aboutFragment, fTag)
            .commit()
    }

}
