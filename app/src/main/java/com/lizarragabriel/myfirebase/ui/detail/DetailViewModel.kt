package com.lizarragabriel.myfirebase.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lizarragabriel.myfirebase.entity.Restaurant
import com.lizarragabriel.myfirebase.entity.Review
import com.lizarragabriel.myfirebase.repository.Repository
import com.lizarragabriel.myfirebase.utils.Val
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    // selected restaurant
    private var _restaurant = MutableLiveData(Val.myRestaurant)
    val restaurant: LiveData<Restaurant> get() = _restaurant

    // loading progress
    private var _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    // restaurant rating
    private var _rating = MutableLiveData<Int>()
    val rating: LiveData<Int> get() = _rating

    // my review
    private var _comment = MutableLiveData<String>()
    val comment: LiveData<String> get() = _comment

    private var _myRating = MutableLiveData<Int>()
    val myRating: LiveData<Int> = _myRating

    private var myUser = Firebase.auth.currentUser

    // review
    private var myReview: Review? = null

    // restaurant
    private val myRestaurant = Val.myRestaurant

    // progress
    private var myProgress = 0

    // comentario
    private var myComentario = ""

    // restaurant review list
    val reviewList: LiveData<List<Review>> get() = _reviewList
    private val _reviewList: MutableLiveData<List<Review>> by lazy {
        MutableLiveData<List<Review>>().also {
            _loading.postValue(true)

            viewModelScope.launch(Dispatchers.IO) {
                delay(500L)
                try {
                    val task = repository.loadReviews(myRestaurant.id!!)
                    task.addSnapshotListener { value, _ ->
                        if(value!!.isEmpty) {
                            it.postValue(emptyList())
                            return@addSnapshotListener
                        }

                        // convert snapshot to a list
                        val myReviewList = ArrayList<Review>()
                        for((i, review) in value.withIndex()) {
                            myReviewList.add(review.toObject(Review::class.java))
                            myReviewList[i].id = review.id
                        }
                        it.postValue(myReviewList)

                        // find if a review is alredy posted
                        for(review in myReviewList) {
                            if(review.usuario == myUser!!.email) {
                                _comment.postValue(review.comentario!!)
                                _myRating.postValue(review.raiting!!)

                                myReview = review
                                break
                            }
                        }

                        // find starts in restaurant
                        if(myReviewList.isNotEmpty()) {
                            var acumulate = 0
                            for(review in myReviewList) {
                                acumulate += review.raiting!!
                            }
                            val newNum = acumulate / myReviewList.size
                            println("acumulate: $acumulate total: $newNum")
                            _rating.postValue(acumulate / myReviewList.size)
                        }
                    }
                    _loading.postValue(false)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Val.myLog("ERROR ${e.message}")
                    _loading.postValue(false)
                    it.postValue(emptyList())
                }
            }
        }
    }

    fun mAddButton(progress: Int, myComment: String) {
        if(progress == 0) {
            return
        }
        if(myComment.isEmpty()) {
            return
        }
        myProgress = progress
        myComentario = myComment

        if(myReview != null) {
            mUpdate()
            return
        }
        mAddReview()
    }

    private fun mAddReview() {
        val review = Review(myComentario, myProgress, restaurant.value!!.id, myUser!!.email,
            myReview?.id, myUser!!.photoUrl.toString())
        try {
            val task = repository.addReview(review)
            task
                .addOnSuccessListener {
                    myReview = review
                    myReview!!.id = it.id
                }
        } catch (e: Exception) {
            Val.myLog("Error: ${e.message}")
        }
    }

    private fun mUpdate() {
        myReview!!.raiting = myProgress
        myReview!!.comentario = myComentario
        try {
            val response = repository.updateReview(myReview!!)
        } catch (e: Exception) {
            println("Error. ${e.message}")
        }
    }
}