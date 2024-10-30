package ch.heigvd.iict.daa.lab03

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ch.heigvd.iict.daa.labo3.Student
import ch.heigvd.iict.daa.labo3.Worker
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    private lateinit var calendar: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Base fields
        val baseEditTexts = listOf(
            findViewById<EditText>(R.id.main_base_name),
            findViewById<EditText>(R.id.main_base_firstname),
        )

        // Additional fields
        val additionalEditTexts = listOf(
            findViewById<EditText>(R.id.main_complementary_email),
            findViewById<EditText>(R.id.main_complementary_remarks),
        )

        // Nationalities spinner
        val nationalitiesSpinner = findViewById<Spinner>(R.id.nationality)
        val nationalitiesAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.nationalities,
            android.R.layout.simple_spinner_item
        )
        nationalitiesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        nationalitiesSpinner.adapter = nationalitiesAdapter

        // Sectors spinner
        val sectorsSpinner = findViewById<Spinner>(R.id.sector)
        val sectorsAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.sectors,
            android.R.layout.simple_spinner_item
        )
        sectorsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sectorsSpinner.adapter = sectorsAdapter

        // Student fields
        val studentTextViews = listOf(
            findViewById<TextView>(R.id.main_specific_students_title),
            findViewById<TextView>(R.id.main_specific_school_title),
            findViewById<TextView>(R.id.main_specific_graduationyear_title),
        )
        val studentEditTexts = listOf(
            findViewById<EditText>(R.id.main_specific_school),
            findViewById<EditText>(R.id.main_specific_graduationyear),
        )

        // Employee fields
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

        // Hide all student and employee fields
        studentTextViews.forEach { it.visibility = View.GONE }
        studentEditTexts.forEach { it.visibility = View.GONE }
        employeeTextViews.forEach { it.visibility = View.GONE }
        employeeEditTexts.forEach { it.visibility = View.GONE }
        sectorsSpinner.visibility = View.GONE

        // Radio buttons
        val studentRadioButton = findViewById<Button>(R.id.main_base_occupation_student)
        studentRadioButton.setOnClickListener {
            studentTextViews.forEach { it.visibility = View.VISIBLE }
            studentEditTexts.forEach { it.visibility = View.VISIBLE }
            employeeTextViews.forEach { it.visibility = View.GONE }
            employeeEditTexts.forEach { it.visibility = View.GONE }
            sectorsSpinner.visibility = View.GONE
        }

        val employeeRadioButton = findViewById<Button>(R.id.main_base_occupation_worker)
        employeeRadioButton.setOnClickListener {
            studentTextViews.forEach { it.visibility = View.GONE }
            studentEditTexts.forEach { it.visibility = View.GONE }
            employeeTextViews.forEach { it.visibility = View.VISIBLE }
            employeeEditTexts.forEach { it.visibility = View.VISIBLE }
            sectorsSpinner.visibility = View.VISIBLE
        }

        // Cancel button
        val cancelButton = findViewById<Button>(R.id.btn_cancel)
        cancelButton.setOnClickListener {
            baseEditTexts.forEach { it.text.clear() }
            studentEditTexts.forEach { it.text.clear() }
            employeeEditTexts.forEach { it.text.clear() }
            additionalEditTexts.forEach { it.text.clear() }
        }

        // OK button
        val okButton = findViewById<Button>(R.id.btn_ok)
        okButton.setOnClickListener {
            //TODO check that fields are not null or have valid values ?
            val Person = if (studentRadioButton.isSelected) {
                Student(
                    baseEditTexts[0].text.toString(),
                    baseEditTexts[1].text.toString(),
                    calendar,
                    nationalitiesSpinner.selectedItem.toString(),
                    studentEditTexts[0].text.toString(),
                    studentEditTexts[1].text.toString().toInt(),
                    additionalEditTexts[0].text.toString(),
                    additionalEditTexts[1].text.toString()
                )
            } else {
                Worker(
                    baseEditTexts[0].text.toString(),
                    baseEditTexts[1].text.toString(),
                    calendar,
                    nationalitiesSpinner.toString(),
                    employeeEditTexts[0].text.toString(),
                    sectorsSpinner.selectedItem.toString(),
                    employeeEditTexts[1].text.toString().toInt(),
                    additionalEditTexts[0].text.toString(),
                    additionalEditTexts[1].text.toString()
                )
            }

            println("created : $Person")
        }

        // Calendar and DatePickerDialog
        calendar = Calendar.getInstance()
        val cakeButton = findViewById<ImageButton>(R.id.cake)
        cakeButton.setOnClickListener {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog =
                DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                    calendar.set(selectedYear, selectedMonth, selectedDay)
                    val birthdateTextView = findViewById<TextView>(R.id.main_base_birthdate)
                    birthdateTextView.text = String.format("$selectedDay/${selectedMonth + 1}/$selectedYear")
                }, year, month, day)

            datePickerDialog.show()
        }
    }
}