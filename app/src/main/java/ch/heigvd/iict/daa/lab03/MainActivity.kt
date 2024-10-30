package ch.heigvd.iict.daa.lab03

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
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

        // Radio buttons
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

        //TODO spinners setup

        // Cancel button
        val cancelButton = findViewById<Button>(R.id.btn_cancel)
        cancelButton.setOnClickListener {
            //TODO clear all other fields
            studentEditTexts.forEach { it.text.clear() }
            employeeEditTexts.forEach { it.text.clear() }
        }

        // OK button
        val okButton = findViewById<Button>(R.id.btn_ok)
        okButton.setOnClickListener {
            //TODO check that fields are not null ?
            val Person = if (studentRadioButton.isSelected) {
                Student(
                    findViewById<EditText>(R.id.main_base_name).text.toString(),
                    findViewById<EditText>(R.id.main_base_firstname).text.toString(),
                    calendar,
                    findViewById<EditText>(R.id.nationality).toString(),
                    findViewById<EditText>(R.id.main_specific_school).text.toString(),
                    findViewById<EditText>(R.id.main_specific_graduationyear).text.toString().toInt(),
                    findViewById<EditText>(R.id.main_complementary_email).text.toString(),
                    findViewById<EditText>(R.id.main_complementary_remarks).text.toString()
                )
            } else {
                Worker(
                    findViewById<EditText>(R.id.main_base_name).text.toString(),
                    findViewById<EditText>(R.id.main_base_firstname).text.toString(),
                    calendar,
                    findViewById<EditText>(R.id.nationality).toString(),
                    findViewById<EditText>(R.id.main_specific_company).text.toString(),
                    findViewById<Spinner>(R.id.sector).selectedItem.toString(),
                    findViewById<EditText>(R.id.main_specific_experience).text.toString().toInt(),
                    findViewById<EditText>(R.id.main_complementary_email).text.toString(),
                    findViewById<EditText>(R.id.main_complementary_remarks).text.toString()
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
                    birthdateTextView.text = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                }, year, month, day)

            datePickerDialog.show()
        }
    }
}