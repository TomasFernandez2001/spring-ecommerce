package com.ecommerce.controller;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.model.Producto;
import com.ecommerce.model.Usuario;
import com.ecommerce.service.ProductoService;
import com.ecommerce.service.UploadFileService;

@Controller
@RequestMapping("/productos")
public class ProductoController {
	
	private final Logger LOGGER = LoggerFactory.getLogger(ProductoController.class);
	
	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private UploadFileService upload;
		
	
	
	@GetMapping("")
	public String show(Model model) {
		model.addAttribute("productos", productoService.findAll());
		return "productos/show";
		
	}
	@GetMapping("/create")
	public String create() {
		return "productos/create";
		
	}
	
	@PostMapping("/save")
	public String save(Producto producto, @RequestParam("img") MultipartFile file) throws IOException {
		LOGGER.info("este es el objeto producto {}", producto);
		Usuario u = new Usuario(1, "", "", "", "", "", "", "");
		producto.setUsuario(u);
		
		if (!file.isEmpty()) {
			String nombreImagen = upload.saveImage(file);
			producto.setImagen(nombreImagen);
		} else {
			
		}
		
		productoService.save(producto);
		return "redirect:/productos";
	}
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable Integer id, Model model) {
		Producto producto = new Producto();
		Optional<Producto> optionalProducto=productoService.get(id);
		producto = optionalProducto.get();
		
		LOGGER.info("Producto buscado: {}" ,producto);
		model.addAttribute("producto", producto);
		return "productos/edit";
	}
	
	@PostMapping("/update")
	public String update(Producto producto, @RequestParam("img") MultipartFile file) throws IOException {
	    // Obtener el producto actual de la base de datos
	    Producto p = productoService.get(producto.getId()).get();
	    
	    if (!file.isEmpty()) {
	        // Si se sube una nueva imagen, se guarda y se actualiza la imagen del producto
	        String nombreImagen = upload.saveImage(file);
	        producto.setImagen(nombreImagen);

	        // Eliminar la imagen anterior si no es la imagen por defecto
	        if (!p.getImagen().equals("default.jpg")) {
	            upload.deleteImage(p.getImagen());
	        }
	    } else {
	        // Si no se sube una nueva imagen, mantener la imagen existente
	        producto.setImagen(p.getImagen());
	    }
	    
	    // Actualizar el producto en la base de datos
	    producto.setUsuario(p.getUsuario());
	    productoService.update(producto);
	    return "redirect:/productos";
	}

	@PostMapping("/delete/{id}")
	public String delete(@PathVariable Integer id) {
	    Producto p = new Producto();
	    p = productoService.get(id).get();
	    if (p.getImagen().equals("default.jpg")) {
	    	upload.deleteImage(p.getImagen());
			
		}
		
		productoService.delete(id);
	    return "redirect:/productos";
	}

	
}
