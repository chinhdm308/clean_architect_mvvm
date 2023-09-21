package com.base.presentation.core.base

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.base.domain.utils.Resource
import com.base.presentation.R
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.UnknownHostException
import java.nio.charset.Charset

abstract class BaseViewModel : ViewModel() {
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.e("COROUTINE EXCEPTION: $throwable")
    }

    private val _isLoading = MutableStateFlow(false)
    val isLoading get() = _isLoading.asStateFlow()

    private val _errorMessage = MutableSharedFlow<BaseErrorState>()
    val errorMessage get() = _errorMessage.asSharedFlow()

    private var jobHandler: Job? = null

    private fun isLoading(loading: Boolean) = viewModelScope.launch {
        _isLoading.emit(loading)
    }

    fun <ResultType> executeDataFlow(
        data: suspend () -> Flow<Resource<ResultType>>,
        isLoading: Boolean = true,
        result: (Resource<ResultType>) -> Unit,
    ) {
        if (jobHandler?.isActive == true) return

        jobHandler = viewModelScope.launch {
            data.invoke().collect {
                when (it) {
                    is Resource.Success -> result.invoke(Resource.Success(it.data))
                    is Resource.Error -> result.invoke(Resource.Error(it.message, it.code))
                    is Resource.Loading -> if (isLoading) isLoading(it.loading)
                }
            }
        }
    }

    suspend fun handleError(error: Throwable, errorMes: (suspend (Int) -> Any?)? = null) {
        val errorCommon = BaseErrorState.Error(
            title = R.string.alert_title_error_unknown,
            message = R.string.alert_message_error_unknown,
        )
        when (error) {
            is ConnectException -> {}
            is UnknownHostException -> {}
            is HttpException -> {
                val errorBody = error.response()?.errorBody()
                val source = errorBody?.source()
                source?.request(Long.MAX_VALUE)
                val buffer = source?.buffer
                val responseBodyString = buffer?.clone()?.readString(Charset.forName("UTF-8"))
                Timber.e(responseBodyString)
                if (error.code() != HttpURLConnection.HTTP_UNAUTHORIZED) {
                    when (val errorMessage = errorMes?.invoke(error.code())) {
                        is Int -> {
                            _errorMessage.emit(BaseErrorState.Error(message = errorMessage))
                        }

                        is BaseErrorState.Error -> _errorMessage.emit(errorMessage)
                        BaseErrorState.ErrorCommon -> _errorMessage.emit(errorCommon)
                    }
                }
            }

            else -> _errorMessage.emit(errorCommon)
        }
    }
}

sealed class BaseErrorState {
    data class Error(
        @StringRes val title: Int? = null,
        @StringRes val message: Int? = null,
    ) : BaseErrorState()

    data object ErrorCommon : BaseErrorState()
}