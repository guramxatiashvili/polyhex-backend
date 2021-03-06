package com.robiumautomations.polyhex.controllers

import com.robiumautomations.polyhex.models.UserCredentials
import com.robiumautomations.polyhex.security.CustomAuthentication
import com.robiumautomations.polyhex.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.*

@RestController
class AuthenticationController {

  @Autowired
  private lateinit var customAuthentication: CustomAuthentication

  @PostMapping("/signin")
  fun singIn(@RequestBody user: UserCredentials): ResponseEntity<String> {
    if (user.username.isNullOrEmpty()) {
      return ResponseEntity("Specify username.", HttpStatus.BAD_REQUEST)
    }
    if (user.password.isNullOrEmpty()) {
      return ResponseEntity("Specify password.", HttpStatus.BAD_REQUEST)
    }
    val token: String
    return try {
      token = customAuthentication.attemptAuthentication(user.username, user.password)
      val headers = HttpHeaders()
      headers.add("Authorization", token)
      ResponseEntity(headers, HttpStatus.OK)
    } catch (e: AuthenticationException) {
      e.printStackTrace()
      ResponseEntity("Invalid credentials.", HttpStatus.BAD_REQUEST)
    }
  }
}