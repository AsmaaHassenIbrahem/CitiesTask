package asma.cities.coordinates.base

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

import androidx.fragment.app.Fragment
import asma.cities.coordinates.EasyParkApplication
import asma.cities.coordinates.R
import asma.cities.coordinates.utilities.Constants
import com.tapadoo.alerter.Alerter

abstract class BaseFragment<Binding : ViewDataBinding, ViewModel : BaseViewModel> : BaseFunc() {
    protected abstract val viewModel: ViewModel
    protected lateinit var binding: Binding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, layout(), container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.setVariable(Constants.EndPoint.viewModel, viewModel)
        return binding.root
    }

    fun showNetworkError(activity: Activity?){
        Alerter.create(activity)
            .setTitle(R.string.error_network_title)
            .enableInfiniteDuration(true)
            .setEnterAnimation(R.anim.alerter_slide_in_from_top)
            .setExitAnimation(R.anim.alerter_slide_out_to_top)
            .setText(R.string.error_network_msg)
            .addButton(EasyParkApplication.applicationContext().resources.getString(R.string.dismiss), R.style.AlertButton) { Alerter.hide() }
            .addButton(EasyParkApplication.applicationContext().resources.getString(R.string.retry)  , R.style.AlertButton) {
                Alerter.hide()
                viewModel.handleRetry()
            }
            .show()
    }

    fun showError(activity: Activity?, msg : String?){
        Alerter.create(activity)
            .setTitle(R.string.error)
            .enableInfiniteDuration(true)
            .setEnterAnimation(R.anim.alerter_slide_in_from_top)
            .setExitAnimation(R.anim.alerter_slide_out_to_top)
            .setText(msg.toString())
            .addButton(EasyParkApplication.applicationContext().resources.getString(R.string.dismiss), R.style.AlertButton) { Alerter.hide() }
            .addButton(EasyParkApplication.applicationContext().resources.getString(R.string.retry)  , R.style.AlertButton) {
                Alerter.hide()
                viewModel.handleRetry() }
            .show()
    }


}

abstract class BaseMapFragment<Binding : ViewDataBinding> : BaseFunc() {
    private lateinit var binding: Binding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, layout(), container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
}


abstract class BaseFunc : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    @LayoutRes
    abstract fun layout(): Int

    abstract fun init()
}
