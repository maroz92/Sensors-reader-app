package pl.mrozok.sensorsreader.presentation

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_readings.*
import kotlinx.android.synthetic.main.content_readings.*
import pl.mrozok.core.infrastructure.Sensor.Type
import pl.mrozok.core.presentation.ReadingsScreen
import pl.mrozok.core.presentation.ReadingsScreen.ViewModel
import pl.mrozok.sensorsreader.R
import pl.mrozok.sensorsreader.di.SensorsModule
import pl.mrozok.sensorsreader.presentation.tools.BaseActivity
import javax.inject.Inject

class ReadingsActivity : BaseActivity(), ReadingsScreen.UI {

    // TODO clean up strings and extract to strings

    @Inject
    lateinit var presenter: ReadingsScreen.Presenter
    private var shownDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_readings)
        setSupportActionBar(toolbar)

        injectDependencies()

        presenter.attachUi(this)

        post_data.setOnClickListener {
            shownDialog = ProgressDialog.show(this) { presenter.cancelSending() }
            presenter.sendToBackend()
        }
        clear_data.setOnClickListener { presenter.clearReadings() }
        setupFilterDropDown()
    }

    private fun injectDependencies() {
        rootComponent().activitySubcomponent()
            .sensorsModule(SensorsModule(readings_container))
            .build().inject(this)
    }

    private fun setupFilterDropDown() {
        ArrayAdapter.createFromResource(
            this,
            R.array.filters_sensor_names,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            selected_filter.adapter = adapter
        }
        val filterItems = arrayOf("", Type.ACCELEROMETER, Type.SCREEN_TOUCH)
        selected_filter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                presenter.applyFilter(filterItems[pos])
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.startListening()
    }

    override fun onPause() {
        presenter.stopListening()
        super.onPause()
    }

    override fun updateUI(viewModel: ViewModel) {
        when (viewModel) {
            is ViewModel.Readings -> {
                val data = StringBuilder()
                viewModel.data.forEach { item ->
                    data.appendln(item)
                }
                readings.text = data
            }
            is ViewModel.Error -> {
                hideDialog()
                showSnackbar("Error occurred, please try again")
                viewModel.exception.printStackTrace()
            }
            is ViewModel.ReadingsPosted -> {
                hideDialog()
                showSnackbar("Readings successfully published")
            }
        }
    }

    private fun hideDialog() {
        shownDialog?.dismiss()
        shownDialog = null
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(
            readings_container,
            message,
            Snackbar.LENGTH_SHORT
        )
            .setAction("OK") { /* just for sake of closing snackbar  */ }
            .show()
    }

}
