package com.drnkgn.juicejuicejuice.screens.settings.tagSettings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drnkgn.juicejuicejuice.db.AppDatabase
import com.drnkgn.juicejuicejuice.db.entities.Tag
import com.drnkgn.juicejuicejuice.enums.UiState
import com.drnkgn.juicejuicejuice.utils.UiStateHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class TagSettingsViewModel @Inject constructor(
    private val database: AppDatabase
): ViewModel() {
    private val tagDao = database.tag()

    val createTagState = UiStateHolder<Unit>(UiState.Idle)
    val updateTagState = UiStateHolder<Unit>(UiState.Idle)
    val removeTagState = UiStateHolder<Unit>(UiState.Idle)
    val getTagState = UiStateHolder<Tag>(UiState.Idle)
    val getAllTagsState = UiStateHolder<List<Tag>>(UiState.Idle)

    fun getAllTags() {
        viewModelScope.launch {
            getAllTagsState.set(UiState.Loading)
            getAllTagsState.set(UiState.Success(tagDao.getAllTagsWithTrashed()))
        }
    }

    fun createTag(tag: Tag) {
        viewModelScope.launch {
            createTagState.set(UiState.Loading)
            tagDao.create(tag)
            createTagState.set(UiState.Success(Unit))
        }
    }

    fun updateTag(tag: Tag) {
        viewModelScope.launch {
            updateTagState.set(UiState.Loading)
            tagDao.update(tag)
            updateTagState.set(UiState.Success(Unit))
        }
    }

    fun removeTag(tag: Tag) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        updateTag(tag.copy(deletedAt = formatter.format(LocalDateTime.now())))
    }

    fun getTag(id: Int) {
        viewModelScope.launch {
            getTagState.set(UiState.Loading)
            val tag = tagDao.findById(id)

            getTagState.set(
                if (tag == null)
                    UiState.Error("No tag found for id $id")
                else
                    UiState.Success(tag)
            )
        }
    }
}