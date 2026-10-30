package com.example.chistopro.data.remote

import com.example.chistopro.data.remote.dto.ErrorResponse
import com.google.gson.Gson
import retrofit2.HttpException
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

/** Ошибка с текстом, который можно показать пользователю. */
class ApiException(message: String) : Exception(message)

/**
 * Выполняет запрос и превращает любые ошибки в Result.failure с понятным сообщением:
 * - ответ сервера с ошибкой -> текст из поля "error" (например, «Неверный email или пароль»);
 * - нет связи -> подсказка проверить API.
 */
suspend fun <T> apiCall(block: suspend () -> T): Result<T> =
    try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: HttpException) {
        Result.failure(ApiException(e.serverMessage()))
    } catch (e: IOException) {
        Result.failure(
            ApiException("Нет связи с сервером. Проверьте, что API запущен и адрес указан верно")
        )
    } catch (e: Exception) {
        Result.failure(ApiException("Непредвиденная ошибка: ${e.message}"))
    }

private fun HttpException.serverMessage(): String {
    val body = response()?.errorBody()?.string()
    val parsed = runCatching { Gson().fromJson(body, ErrorResponse::class.java)?.error }.getOrNull()
    return parsed ?: "Ошибка сервера (${code()})"
}
