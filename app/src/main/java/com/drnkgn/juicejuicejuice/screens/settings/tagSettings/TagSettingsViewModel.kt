package com.drnkgn.juicejuicejuice.screens.settings.tagSettings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drnkgn.juicejuicejuice.db.AppDatabase
import com.drnkgn.juicejuicejuice.db.entities.Tag
import com.drnkgn.juicejuicejuice.states.Resource
import com.drnkgn.juicejuicejuice.states.UiStateHolder
import com.drnkgn.juicejuicejuice.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class TagSettingsViewModel @Inject constructor(
    database: AppDatabase
): ViewModel() {
    private val tagDao = database.tag()

    val createTagState = UiStateHolder<Unit>(Resource.Idle)
    val updateTagState = UiStateHolder<Unit>(Resource.Idle)
    val removeTagState = UiStateHolder<Unit>(Resource.Idle)
    val getTagState = UiStateHolder<Tag>(Resource.Idle)
    val getAllTagsState = UiStateHolder<List<Tag>>(Resource.Idle)

    fun getAllTags() {
        viewModelScope.launch {
            getAllTagsState.set(isLoading = true)
            getAllTagsState.set(
                isLoading = false,
                data = Utils.safeApiCall { tagDao.getAllTagsWithTrashed() }
            )
        }
    }

    fun createTag(tag: Tag) {
        viewModelScope.launch {
            createTagState.set(isLoading = true)
            tagDao.create(tag)
            createTagState.set(isLoading = false, data = Resource.Success(Unit))
        }
    }

    fun updateTag(tag: Tag) {
        viewModelScope.launch {
            updateTagState.set(isLoading = true)
            tagDao.update(tag)
            updateTagState.set(isLoading = false, data = Resource.Success(Unit))
        }
    }

    fun removeTag(tag: Tag) {
        viewModelScope.launch {
            removeTagState.set(isLoading = true)
            tagDao.update(tag.copy(deletedAt = LocalDateTime.now()))
            removeTagState.set(data = Resource.Success(Unit))
        }
    }

    fun getTag(id: Int) {
        viewModelScope.launch {
            getTagState.set(isLoading = true)
            val tag = tagDao.findById(id)

            getTagState.set(
                isLoading = false,
                data = when {
                    tag == null -> {
                        Resource.GenericError(Exception("No tag found for id $id"))
                    }
                    else -> Resource.Success(tag)
                }
            )
        }
    }
}