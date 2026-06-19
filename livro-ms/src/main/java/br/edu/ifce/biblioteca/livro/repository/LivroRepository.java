package br.edu.ifce.biblioteca.livro.repository;

import br.edu.ifce.biblioteca.livro.entity.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LivroRepository extends JpaRepository<Livro, Long> {

    List<Livro> findByDisponivel(boolean disponivel);
}
