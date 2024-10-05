package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/libros")
public class LibroController {

    @Autowired
    private LibroRepository LibroRepository;

    // Página de inicio
    @GetMapping("/libros")
    public String inicio() {
        return "index";
    }

    // Obtener todos los libros
    @GetMapping
    @ResponseBody
    public ResponseEntity<List<Libros>> obtenerLibros() {
        List<Libros> libros = LibroRepository.findAll();
        return new ResponseEntity<>(libros, HttpStatus.OK);
    }

    // Obtener un libro por ID
    @GetMapping("/libros/{id}")
    @ResponseBody
    public ResponseEntity<Libros> obtenerLibroPorId(@PathVariable int id) {
        Optional<Libros> libro = LibroRepository.findById(id);
        return libro.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Agregar un nuevo libro
    @PostMapping
    @ResponseBody
    public ResponseEntity<Libros> agregarLibro(@RequestBody Libros nuevoLibro) {
        Libros libroGuardado = LibroRepository.save(nuevoLibro);
        return new ResponseEntity<>(libroGuardado, HttpStatus.CREATED);
    }

    // Actualizar un libro existente
    @PutMapping("/libros/{id}")
    @ResponseBody
    public ResponseEntity<String> actualizarLibro(@PathVariable int id, @RequestBody Libros libroActualizado) {
        Optional<Libros> libroOpt = LibroRepository.findById(id);

        if (libroOpt.isPresent()) {
            Libros libroExistente = libroOpt.get();

            // Solo actualiza si los valores están proporcionados y no son nulos o vacíos
            if (libroActualizado.getTitulo() != null && !libroActualizado.getTitulo().isEmpty()) {
                libroExistente.setTitulo(libroActualizado.getTitulo());
            }
            if (libroActualizado.getAutor() != null && !libroActualizado.getAutor().isEmpty()) {
                libroExistente.setAutor(libroActualizado.getAutor());
            }

            // Guarda el libro actualizado
            LibroRepository.save(libroExistente);

            return new ResponseEntity<>("Libro actualizado exitosamente", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Libro no encontrado", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/libros/{id}")
    @ResponseBody
    public ResponseEntity<String> eliminarLibroConInteger(@PathVariable Integer id) {
        if (LibroRepository.existsById(id)) {
            LibroRepository.deleteById(id);
            return new ResponseEntity<>("Libro eliminado exitosamente", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Libro no encontrado", HttpStatus.NOT_FOUND);
        }
    }
    

    // Obtener libro por título
    @GetMapping("/libros/titulo/{titulo}")
    @ResponseBody
    public ResponseEntity<Libros> obtenerLibroPorTitulo(@PathVariable String titulo) {
        Optional<Libros> libro = LibroRepository.findByTitulo(titulo);
        return libro.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}

