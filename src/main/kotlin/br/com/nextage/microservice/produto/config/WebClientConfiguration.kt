package br.com.nextage.microservice.produto.config

import com.fasterxml.jackson.databind.ObjectMapper
import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.Connection
import reactor.netty.http.client.HttpClient
import java.util.concurrent.TimeUnit

@Configuration
class SpringBootRestClientsCheatsheetsConfiguration {

    @Value("\${client.timeout}")
    private val timeout: Long = 0

    @Value("\${client.memory-size}")
    private val memorySize: Int? = null

    @Bean
    fun messageConverter(objectMapper: ObjectMapper): MessageConverter {
        return Jackson2JsonMessageConverter(objectMapper)
    }

    @Bean
    fun webClient(objectMapper: ObjectMapper): WebClient =
            this.createWebClient(objectMapper)

    @Bean
    fun webClientLowerCamelCase(@Qualifier("objectMapperLowerCamelCase") objectMapper: ObjectMapper): WebClient =
            this.createWebClient(objectMapper)


    fun createWebClient(objectMapper: ObjectMapper) =
            WebClient.builder()
                    .codecs {
                        it.defaultCodecs().jackson2JsonEncoder(Jackson2JsonEncoder(objectMapper, MediaType.APPLICATION_JSON))
                        it.defaultCodecs().jackson2JsonDecoder(Jackson2JsonDecoder(objectMapper, MediaType.APPLICATION_JSON))
                        it.defaultCodecs().maxInMemorySize(memorySize!!)
                    }
                    .clientConnector(
                            ReactorClientHttpConnector(
                                    HttpClient
                                            .create()
                                            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeout.toInt())
                                            .doOnConnected { connection: Connection ->
                                                connection.addHandlerLast(ReadTimeoutHandler(timeout, TimeUnit.MILLISECONDS))
                                                connection.addHandlerLast(WriteTimeoutHandler(timeout, TimeUnit.MILLISECONDS))
                                            }
                            )
                    )
                    .build()
}
