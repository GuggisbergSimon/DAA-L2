package ch.heigvd.iict.daa.lab03

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Students and employees fields
        val studentTextViews = listOf(
            findViewById<TextView>(R.id.main_specific_students_title),
            findViewById<TextView>(R.id.main_specific_school_title),
            findViewById<TextView>(R.id.main_specific_graduationyear_title),
        )
        val studentEditTexts = listOf(
            findViewById<EditText>(R.id.main_specific_school),
            findViewById<EditText>(R.id.main_specific_graduationyear),
        )

        val employeeTextViews = listOf(
            findViewById<TextView>(R.id.main_specific_workers_title),
            findViewById<TextView>(R.id.main_specific_company_title),
            findViewById<TextView>(R.id.main_specific_sector_title),
            findViewById<TextView>(R.id.main_specific_experience_title),
        )
        val employeeEditTexts = listOf(
            findViewById<EditText>(R.id.main_specific_company),
            findViewById<EditText>(R.id.main_specific_experience),
        )
        val employeeSpinner = findViewById<Spinner>(R.id.sector)

        // Hide all student and employee fields
        studentTextViews.forEach { it.visibility = View.GONE }
        studentEditTexts.forEach { it.visibility = View.GONE }
        employeeTextViews.forEach { it.visibility = View.GONE }
        employeeEditTexts.forEach { it.visibility = View.GONE }
        employeeSpinner.visibility = View.GONE

        val studentRadioButton = findViewById<Button>(R.id.main_base_occupation_student)
        studentRadioButton.setOnClickListener {
            studentTextViews.forEach { it.visibility = View.VISIBLE }
            studentEditTexts.forEach { it.visibility = View.VISIBLE }
            employeeTextViews.forEach { it.visibility = View.GONE }
            employeeEditTexts.forEach { it.visibility = View.GONE }
            employeeSpinner.visibility = View.GONE
        }

        val employeeRadioButton = findViewById<Button>(R.id.main_base_occupation_worker)
        employeeRadioButton.setOnClickListener {
            studentTextViews.forEach { it.visibility = View.GONE }
            studentEditTexts.forEach { it.visibility = View.GONE }
            employeeTextViews.forEach { it.visibility = View.VISIBLE }
            employeeEditTexts.forEach { it.visibility = View.VISIBLE }
            employeeSpinner.visibility = View.VISIBLE
        }

        val cancelButton = findViewById<Button>(R.id.btn_cancel)
        cancelButton.setOnClickListener {
            //TODO clear all other fields
            studentEditTexts.forEach { it.text.clear() }
            employeeEditTexts.forEach { it.text.clear() }
        }

        val okButton = findViewById<Button>(R.id.btn_ok)
        okButton.setOnClickListener {
            //TODO add the new person to the list
        }

        val cakeButton = findViewById<ImageButton>(R.id.cake)
        cakeButton.setOnClickListener {
            //TODO call datepicker API
        }
    }
}