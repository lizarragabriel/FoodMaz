package com.lizarragabriel.myfirebase.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lizarragabriel.myfirebase.entity.Restaurant
import com.lizarragabriel.myfirebase.repository.Repository
import com.lizarragabriel.myfirebase.utils.Val
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private var _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    val listRestaurant: LiveData<List<Restaurant>> get() = _listRestaurant
    private val _listRestaurant: MutableLiveData<List<Restaurant>> by lazy {
        MutableLiveData<List<Restaurant>>().also {
            _loading.postValue(true)
            viewModelScope.launch(Dispatchers.IO) {
                delay(750L)
                try {
                    val task = repository.loadRestaurants()
                    task.orderBy("name").addSnapshotListener { value, error ->
                        if(value!!.isEmpty) {
                            it.postValue(emptyList())
                            return@addSnapshotListener
                        } else {
                            val myList = ArrayList<Restaurant>()
                            for ((i, restaurant) in value.withIndex()) {
                                myList.add(restaurant.toObject(Restaurant::class.java))
                                myList[i].id = restaurant.id
                            }
                            it.postValue(myList)
                        }
                    }
                    _loading.postValue(false)
                } catch (e: Exception) {
                    _loading.postValue(false)
                    Val.myLog("ERROR ${e.message}")
                    it.postValue(emptyList())
                }
            }
        }
    }
}