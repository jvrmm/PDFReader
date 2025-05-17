package com.reader.pdfreader

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import com.github.barteksc.pdfviewer.util.FitPolicy
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.reader.pdfreader.databinding.ActivityMainBinding

/**
 * Simple App that open any pdf file from your Phone Storage.
 * Toolbar and the open button hide when the user clicks on the pdf view.
 * Components become visible again when the user repeats the action.
 * Minimum api supported: 24. Target api: 35. Tested with api 28 and api 36.
 * Using barteksc's PDFViewer library on github.
 * Coded by Javdev, 2025.
 * For educational purposes only.
 */

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var imageView: ImageView
    private lateinit var textView: TextView
    private lateinit var pdfView: PDFView
    private lateinit var toolbar: MaterialToolbar
    private lateinit var btnOpenPDF: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeComponents()

        binding.btnOpenPDF.setOnClickListener{
            launchPDF.launch("application/pdf")
        }
    }

    fun initializeComponents() {
        btnOpenPDF = findViewById(R.id.btnOpenPDF)
        imageView = findViewById(R.id.imageView)
        textView = findViewById(R.id.mainMessage)
        toolbar = findViewById(R.id.materialToolbar)
        pdfView = findViewById<PDFView>(R.id.pdfView)
    }

    /**
     * Alternate between show/hide the toolbar and fab when click on the pdf view.
     */
    fun switchingComponents() {
        binding.pdfView.setOnClickListener(View.OnClickListener{
            if(toolbar.isVisible) {
                toolbar.isVisible = false
                btnOpenPDF.isVisible = false
            }
            else {
                toolbar.isVisible = true
                btnOpenPDF.isVisible = true
            }
        })
    }

    private val launchPDF = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->

        uri?.let {
            binding.pdfView.fromUri(it)
                .spacing(12)
                .defaultPage(0)
                .enableDoubletap(true)
                .scrollHandle(DefaultScrollHandle(this))
                .pageFitPolicy(FitPolicy.BOTH)
                .autoSpacing(true)
                .enableSwipe(true)
                .load()
            binding.pdfView.useBestQuality(true)

            imageView.isVisible = false
            textView.isVisible = false

            switchingComponents()
        }
    }
}