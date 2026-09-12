package io.legado.app.ui.main.rss

import android.os.Bundle
import io.legado.app.R
import io.legado.app.base.BaseActivity
import io.legado.app.databinding.ActivityRssBinding
import io.legado.app.lib.theme.titleTextColor
import io.legado.app.ui.widget.TitleBar
import io.legado.app.utils.viewbindingdelegate.viewBinding

/**
 * 单独打开的订阅页。
 * 订阅不再占用底部导航，改从「我的 → 订阅」进入，界面和以前的订阅 Tab 完全一样。
 */
class RssActivity : BaseActivity<ActivityRssBinding>() {

    override val binding by viewBinding(ActivityRssBinding::inflate)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        if (supportFragmentManager.findFragmentById(R.id.fl_fragment) == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fl_fragment, RssFragment())
                .commitNow()
        }
        supportFragmentManager.findFragmentById(R.id.fl_fragment)
            ?.view
            ?.findViewById<TitleBar>(R.id.title_bar)
            ?.apply {
                toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
                setNavigationOnClickListener { finish() }
                setColorFilter(titleTextColor)
            }
    }

}
