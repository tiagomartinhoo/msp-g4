package com.msp.api.http.pipeline.authentication

import com.msp.api.domain.User
import com.msp.api.http.pipeline.exceptionHandler.exceptions.Unauthenticated
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor

@Component
class AuthenticationInterceptor(
    private val authorizationHeaderProcessor: AuthorizationHeaderProcessor
) : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler is HandlerMethod && handler.hasMethodAnnotation(Authentication::class.java)) {
            val authorizationValue = request.getHeader(NAME_AUTHORIZATION_HEADER)

            val user = authorizationHeaderProcessor.process(authorizationValue) ?: throw Unauthenticated()

            if (handler.methodParameters.any { it.parameterType == User::class.java }) {
                UserArgumentResolver.addUserTo(user, request)
            }
        }
        return true
    }

    companion object {
        private const val NAME_AUTHORIZATION_HEADER = "Authorization"
    }
}
