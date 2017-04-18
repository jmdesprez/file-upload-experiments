package jm.desprez.model

import com.fasterxml.jackson.annotation.JsonProperty

data class Hello(@JsonProperty var message: String? = null)