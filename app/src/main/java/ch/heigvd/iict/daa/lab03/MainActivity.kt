package ch.heigvd.iict.daa.lab03

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var birthday: Calendar
    private var datePickerDialog: DatePickerDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Lookup fields
        val birthdateEditText = findViewById<EditText>(R.id.main_base_birthdate)
        val natSpinner = findViewById<Spinner>(R.id.nationality)
        val secSpinner = findViewById<Spinner>(R.id.sector)
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
            secSpinner.visibility = visibility
        }

        fun setBirthday(year: Int, month: Int, day: Int) {
            birthday.set(year, month, day)
            val dateFormat = java.text.DateFormat.getDateInstance(java.text.DateFormat.LONG, Locale.getDefault())
            birthdateEditText.setText(dateFormat.format(birthday.time))
        }

        fun addChoicesToSpinner(array: Int, spinner: Spinner) {
            val items = resources.getStringArray(array).toMutableList()
            items.add(0, getString(R.string.select))
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                items
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

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
            natSpinner.setSelection(0)
            secSpinner.setSelection(0)
            birthday = Calendar.getInstance()
            setBirthday(
                birthday.get(Calendar.YEAR),
                birthday.get(Calendar.MONTH),
                birthday.get(Calendar.DAY_OF_MONTH)
            )
        }

        // Handle OK button
        val okButton = findViewById<Button>(R.id.btn_ok)
        val radioGroup = findViewById<RadioGroup>(R.id.main_base_occupation)
        okButton.setOnClickListener {
            val nationality = if (natSpinner.selectedItemPosition == 0) null else natSpinner.selectedItem.toString()
            //TODO check that fields are not null or have valid values ?
            val Person = if (radioGroup.checkedRadioButtonId == R.id.main_base_occupation_student) {
                Student(
                    baseEditTexts[0].text.toString(),
                    baseEditTexts[1].text.toString(),
                    birthday,
                    nationality.toString(),
                    studentEditTexts[0].text.toString(),
                    studentEditTexts[1].text.toString().toInt(),
                    additionalEditTexts[0].text.toString(),
                    additionalEditTexts[1].text.toString()
                )
            } else {
                val sector = if (secSpinner.selectedItemPosition == 0) null else secSpinner.selectedItem.toString()
                Worker(
                    baseEditTexts[0].text.toString(),
                    baseEditTexts[1].text.toString(),
                    birthday,
                    nationality.toString(),
                    employeeEditTexts[0].text.toString(),
                    sector.toString(),
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
            val year = birthday.get(Calendar.YEAR)
            val month = birthday.get(Calendar.MONTH)
            val day = birthday.get(Calendar.DAY_OF_MONTH)

            datePickerDialog =
                DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                    setBirthday(selectedYear, selectedMonth, selectedDay)
                }, year, month, day)

            datePickerDialog?.show()
        }

        // Handle actionDone
        additionalEditTexts[1].setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                okButton.performClick()
                true
            } else {
                false
            }
        }

        // Init student and employee visibility
        toggleStudent(View.GONE)
        toggleEmployee(View.GONE)

        // Init Birthdate
        birthday = Calendar.getInstance()
        setBirthday(
            birthday.get(Calendar.YEAR),
            birthday.get(Calendar.MONTH),
            birthday.get(Calendar.DAY_OF_MONTH)
        )

        // Init spinners
        addChoicesToSpinner(R.array.nationalities, natSpinner)
        addChoicesToSpinner(R.array.sectors, secSpinner)

        // exampleWorker
        /*
        toggleEmployee(View.VISIBLE)
        setBirthday(
            Person.exampleWorker.birthDay.get(Calendar.YEAR),
            Person.exampleWorker.birthDay.get(Calendar.MONTH),
            Person.exampleWorker.birthDay.get(Calendar.DAY_OF_MONTH)
        )
        baseEditTexts[0].setText(Person.exampleWorker.name)
        baseEditTexts[1].setText(Person.exampleWorker.firstName)
        natSpinner.setSelection(
            (natSpinner.adapter as ArrayAdapter<String>).getPosition(Person.exampleWorker.nationality))
        radioGroup.check(R.id.main_base_occupation_worker)
        employeeEditTexts[0].setText(Person.exampleWorker.company)
        secSpinner.setSelection(
            (secSpinner.adapter as ArrayAdapter<String>).getPosition(Person.exampleWorker.sector))
        employeeEditTexts[1].setText(Person.exampleWorker.experienceYear.toString())
        additionalEditTexts[0].setText(Person.exampleWorker.email)
        additionalEditTexts[1].setText(Person.exampleWorker.remark)
        */

        // exampleStudent
        /*
        toggleStudent(View.VISIBLE)
        setBirthday(
            Person.exampleStudent.birthDay.get(Calendar.YEAR),
            Person.exampleStudent.birthDay.get(Calendar.MONTH),
            Person.exampleStudent.birthDay.get(Calendar.DAY_OF_MONTH)
        )
        baseEditTexts[0].setText(Person.exampleStudent.name)
        baseEditTexts[1].setText(Person.exampleStudent.firstName)
        natSpinner.setSelection(
            (natSpinner.adapter as ArrayAdapter<String>).getPosition(Person.exampleStudent.nationality))
        radioGroup.check(R.id.main_base_occupation_student)
        studentEditTexts[0].setText(Person.exampleStudent.university)
        studentEditTexts[1].setText(Person.exampleStudent.graduationYear.toString())
        additionalEditTexts[0].setText(Person.exampleStudent.email)
        additionalEditTexts[1].setText(Person.exampleStudent.remark)
         */
    }
    override fun onDestroy() {
        super.onDestroy()
        datePickerDialog?.dismiss()
    }
}