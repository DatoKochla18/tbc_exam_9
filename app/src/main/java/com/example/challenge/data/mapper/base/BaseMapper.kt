package com.example.challenge.data.mapper.base

import com.example.challenge.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

suspend fun <Dto, Domain> Flow<Resource<Dto>>.asResource(
    onSuccess: suspend (Dto) -> Domain,
): Flow<Resource<Domain>> {
    return this.map {
        when (it) {
            is Resource.Success -> Resource.Success(data = onSuccess.invoke(it.data))
            is Resource.Error -> Resource.Error(message = it.message)
            is Resource.Loading -> Resource.Loading
        }
    }
}
