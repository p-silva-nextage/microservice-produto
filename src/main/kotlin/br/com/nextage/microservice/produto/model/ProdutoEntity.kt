package br.com.nextage.microservice.produto.model

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "produto")
data class ProdutoEntity(
        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null
) : Serializable