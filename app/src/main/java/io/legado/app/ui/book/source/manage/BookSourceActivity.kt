package io.legado.app.ui.book.source.manage

import android.os.Bundle
import io.legado.app.R
import io.legado.app.base.BaseActivity
import io.legado.app.databinding.ActivityBookSourceBinding
import io.legado.app.utils.viewbindingdelegate.viewBinding

/**
 * 单独打开的书源管理页（我的、侧边栏等入口）。
 * 真正的界面在 [BookSourceManageFragment]，和底部导航里的「书源」是同一个页面。
 */
class BookSourceActivity : BaseActivity<ActivityBookSourceBinding>() {

    override val binding by viewBinding(ActivityBookSourceBinding::inflate)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        if (supportFragmentManager.findFragmentById(R.id.fl_fragment) == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fl_fragment, BookSourceManageFragment())
                .commit()
        }
    }

}
