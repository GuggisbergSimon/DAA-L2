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
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import java.time.Year
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    private lateinit var calendar: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Lookup fields
        val birthdateTextView = findViewById<EditText>(R.id.main_base_birthdate)
        val nationalitiesSpinner = findViewById<Spinner>(R.id.nationality)
        val sectorsSpinner = findViewById<Spinner>(R.id.sector)
        val baseEditTexts = listOf(
            findViewById<EditText>(R.id.main_base_name),
            findViewById<EditText>(R.id.main_base_firstname),
        )
        val additionalEditTexts = listOf(
            findViewById<EditText>(R.id.main_complementary_email),
            findViewById<EditText>(R.id.main_complementary_remarks),
        )
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

        // Helper functions
        fun toggleStudent(visibility: Int) {
            studentTextViews.forEach { it.visibility = visibility }
            studentEditTexts.forEach { it.visibility = visibility }
        }

        fun toggleEmployee(visibility: Int) {
            employeeTextViews.forEach { it.visibility = visibility }
            employeeEditTexts.forEach { it.visibility = visibility }
            sectorsSpinner.visibility = visibility
        }

        fun setBirthday(year: Int, month: Int, day: Int) {
            calendar.set(year, month, day)
            birthdateTextView.setText(String.format("$day/${month + 1}/$year"))
        }

        fun addChoicesToSpinner(array: Int, spinner: Spinner) {
            val adapter = ArrayAdapter.createFromResource(
                this,
                array,
                android.R.layout.simple_spinner_item
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        // Hide all student and employee fields
        toggleStudent(View.GONE)
        toggleEmployee(View.GONE)

        // Handle Radio buttons
        val studentRadioButton = findViewById<Button>(R.id.main_base_occupation_student)
        studentRadioButton.setOnClickListener {
            toggleStudent(View.VISIBLE)
            toggleEmployee(View.GONE)
        }

        val employeeRadioButton = findViewById<Button>(R.id.main_base_occupation_worker)
        employeeRadioButton.setOnClickListener {
            toggleStudent(View.GONE)
            toggleEmployee(View.VISIBLE)
        }

        // Handle Cancel button
        val cancelButton = findViewById<Button>(R.id.btn_cancel)
        cancelButton.setOnClickListener {
            baseEditTexts.forEach { it.text.clear() }
            studentEditTexts.forEach { it.text.clear() }
            employeeEditTexts.forEach { it.text.clear() }
            additionalEditTexts.forEach { it.text.clear() }
        }

        // Handle OK button
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

        // Handle DatePickerDialog button
        val cakeButton = findViewById<ImageButton>(R.id.cake)
        cakeButton.setOnClickListener {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog =
                DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                    setBirthday(selectedYear, selectedMonth, selectedDay)
                }, year, month, day)

            datePickerDialog.show()
        }

        // Init Birthdate
        calendar = Calendar.getInstance()
        setBirthday(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        // Init spinners
        addChoicesToSpinner(R.array.nationalities, nationalitiesSpinner)
        addChoicesToSpinner(R.array.sectors, sectorsSpinner)

        // exampleWorker
        setBirthday(
            Person.exampleWorker.birthDay.get(Calendar.YEAR),
            Person.exampleWorker.birthDay.get(Calendar.MONTH),
            Person.exampleWorker.birthDay.get(Calendar.DAY_OF_MONTH)
        )
        baseEditTexts[0].setText(Person.exampleWorker.name)
        baseEditTexts[1].setText(Person.exampleWorker.firstName)
        nationalitiesSpinner.setSelection(
            (nationalitiesSpinner.adapter as ArrayAdapter<String>).getPosition(
                Person.exampleWorker))
        employeeEditTexts[0].setText(Person.exampleWorker.company)
        sectorsSpinner.setSelection(
            (sectorsSpinner.adapter as ArrayAdapter<String>).getPosition(
                Person.exampleWorker.sector))
        employeeEditTexts[1].setText(Person.exampleWorker.experienceYear.toString())
        additionalEditTexts[0].setText(Person.exampleWorker.email)
        additionalEditTexts[1].setText(Person.exampleWorker.remark)


        // exampleStudent
        setBirthday(
            Person.exampleStudent.birthDay.get(Calendar.YEAR),
            Person.exampleStudent.birthDay.get(Calendar.MONTH),
            Person.exampleStudent.birthDay.get(Calendar.DAY_OF_MONTH)
        )
        baseEditTexts[0].setText(Person.exampleStudent.name)
        baseEditTexts[1].setText(Person.exampleStudent.firstName)
        nationalitiesSpinner.setSelection(
            (nationalitiesSpinner.adapter as ArrayAdapter<String>).getPosition(
                Person.exampleStudent))
        studentEditTexts[0].setText(Person.exampleStudent.university)
        studentEditTexts[1].setText(Person.exampleStudent.graduationYear.toString())
        additionalEditTexts[0].setText(Person.exampleStudent.email)
        additionalEditTexts[1].setText(Person.exampleStudent.remark)
    }
}