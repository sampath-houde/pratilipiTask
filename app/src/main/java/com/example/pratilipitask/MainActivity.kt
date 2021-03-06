package com.example.pratilipitask

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.pratilipitask.data.database.ContentDatabase
import com.example.pratilipitask.data.entities.Data
import com.example.pratilipitask.data.repo.DataRepo
import com.example.pratilipitask.databinding.ActivityMainBinding
import com.example.pratilipitask.ui.activity.AddActivity
import com.example.pratilipitask.ui.adapters.ContentAdapter
import com.example.pratilipitask.ui.viewmodels.DataViewModel
import com.example.pratilipitask.utils.Constants
import com.example.pratilipitask.utils.DatabaseViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import kotlin.coroutines.CoroutineContext


class MainActivity : AppCompatActivity(),CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: DataViewModel
    private lateinit var contentAdapter: ContentAdapter
    private lateinit var job: Job
    private lateinit var notesList: List<Data>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        job = Job()

        initData()
        initClickListeners()
        initObservers()
    }

    private fun initObservers() {

        viewModel.fetchData()

        launch {
            viewModel.readAllData.collect { list->
                notesList = list
                if(notesList.isEmpty()) {
                    binding.emptyBanner.visibility = View.VISIBLE
                    binding.searchView.visibility = View.GONE
                    binding.recyclerView.visibility = View.GONE
                } else {
                    binding.emptyBanner.visibility = View.GONE
                    binding.searchView.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.VISIBLE
                }
                contentAdapter.setData(notesList)

                binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                    override fun onQueryTextSubmit(p0: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(p0: String?): Boolean {
                        val tempArr = ArrayList<Data>()

                        for(data in notesList){
                            if (data.title.lowercase(Locale.getDefault()).trim()
                                    .contains(p0.toString()) || data.description?.lowercase(
                                    Locale.getDefault()
                                )?.trim()?.contains(p0.toString())!!
                            ) {
                                tempArr.add(data)
                            }
                        }
                        contentAdapter.setData(tempArr)
                        return false
                    }
                })
            }
        }

    }

    private fun initClickListeners() {
        binding.fabBtn.setOnClickListener {
            startActivity(Intent(this, AddActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    private fun initData() {

        //Initialize Adapter and RecyclerView
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        contentAdapter = ContentAdapter(::onNoteClicked)
        binding.recyclerView.adapter = contentAdapter

        //Initialize ViewModel
        val dataSource = ContentDatabase.getDatabase(this).dataDao()
        val repo = DataRepo(dataSource)
        val factory = DatabaseViewModelFactory(repo, application)
        viewModel = ViewModelProvider(this, factory).get(DataViewModel::class.java)
    }
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Timber.d("OnNewIntent", "sdf","fsf")
        initObservers()
    }

    private fun onNoteClicked(id: Long) {
        val intent = Intent(this, AddActivity::class.java)
        intent.putExtra(Constants.INTENT_NOTE_ID, id)
        startActivity(intent)
    }
}

