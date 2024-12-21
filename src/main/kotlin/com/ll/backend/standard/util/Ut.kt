package com.ll.backend.standard.util

import com.fasterxml.jackson.core.type.TypeReference
import com.ll.backend.global.app.AppConfig

class Ut {
    class json {
        companion object {
            fun toString(obj: Any): String {
                return AppConfig.objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
            }

            fun <T> toObj(jsonStr: String, typeReference: TypeReference<T>): T {
                return AppConfig.objectMapper.readValue(jsonStr, typeReference)
            }
        }
    }
}