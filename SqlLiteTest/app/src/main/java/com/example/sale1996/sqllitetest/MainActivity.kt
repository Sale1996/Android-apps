package com.example.sale1996.sqllitetest

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private val DATABASE_NAME = "android_test"

    private val START_YEAR = 2015
    private val END_YEAR = 2018

    private val MAX_RANK = 10.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //importDatabase(DATABASE_NAME)
        //postavljanje granica grafikona
        graph.viewport.setMinX(START_YEAR.toDouble())
        graph.viewport.setMaxX((END_YEAR + 1).toDouble())
        graph.viewport.setMinY(5.0)
        graph.viewport.setMaxY(MAX_RANK + 1)
        graph.viewport.maxXAxisSize = (END_YEAR + 1).toDouble()

    }

    fun searchStudent(view : View){
        doQuery()
    }

    private fun doQuery(){
        val studentName = student_name_edit_text.text.toString()

        val query = "SELECT year, average_rating FROM new_table WHERE name='$studentName'"
        val db = openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null)
        val cursorResult = db.rawQuery(query, null)

        // fill graph
        val series = LineGraphSeries<DataPoint>()
        val maxPoints = 20

        while (cursorResult.moveToNext()){
            // ovde se procesiraju rezultati pretrage
            // kursor je kao nekakav pokazivac na torku u bazi
            val year = cursorResult.getInt(cursorResult.getColumnIndex("year"))
            val averageRating = cursorResult.getDouble(cursorResult.getColumnIndex("average_rating"))

            series.appendData(DataPoint(year.toDouble(), averageRating), false, maxPoints)
            Log.i("salepare", "godina je $year , a prosek je bio : $averageRating")

        }

        //uvek zatvori
        cursorResult.close()
        graph.removeAllSeries()
        graph.addSeries(series)
    }

    private fun importDatabase(dbName: String){
        val db = openOrCreateDatabase(dbName, Context.MODE_PRIVATE, null)
        val resId = resources.getIdentifier(dbName, "raw", packageName)
        val scan = Scanner(resources.openRawResource(resId))


        var query = ""
        while (scan.hasNextLine()){
            val line = scan.nextLine()
            if (line.trim().startsWith("--")) continue
            query += "$line\n"
            if(query.trim().endsWith(";")){
                db.execSQL(query)
                query = ""
            }

        }
    }
}
