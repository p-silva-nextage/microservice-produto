package br.com.nextage.microservice.produto.repository

import br.com.nextage.microservice.produto.model.ProdutoEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProdutoRepository : JpaRepository<ProdutoEntity, Long> {}