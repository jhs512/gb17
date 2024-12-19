package com.ll.backend.standard.util

import com.ll.backend.global.app.AppConfig

class Ut {
    class json {
        companion object {
            fun toString(obj: Any): String {
                return AppConfig.objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
            }
        }
    }
}