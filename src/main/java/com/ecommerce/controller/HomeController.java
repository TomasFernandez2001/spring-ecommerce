package com.ecommerce.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecommerce.model.DetalleOrden;
import com.ecommerce.model.Orden;
import com.ecommerce.model.Producto;
import com.ecommerce.service.ProductoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/")
public class HomeController {

	private final Logger log = LoggerFactory.getLogger(HomeController.class);
	@Autowired
	private ProductoService productoService;

	// para almacenar los detalles de la orden
	List<DetalleOrden> detalles = new ArrayList<DetalleOrden>();

	Orden orden = new Orden();

	@GetMapping
	public String home(Model model) {
		model.addAttribute("productos", productoService.findAll());
		return "usuario/home";
	}

	@GetMapping("productohome/{id}")
	public String productoHome(@PathVariable Integer id, Model model) {
		log.info("id enviado como parametro{} ", id);
		Producto producto = new Producto();
		Optional<Producto> productoOptional = productoService.get(id);
		producto = productoOptional.get();

		model.addAttribute("producto", producto);
		return "usuario/productohome";

	}

	@PostMapping("/cart")
	public String addCart(@RequestParam Integer id, @RequestParam Integer cantidad, Model model) {
		DetalleOrden detalleOrden = new DetalleOrden();
		Producto producto = new Producto();
		double sumatotal = 0;

		Optional<Producto> optionalProducto = productoService.get(id);

		log.info("Producto añadido: {}", optionalProducto.get());
		log.info("Cantidad: {}", cantidad);

		producto = optionalProducto.get();

		// Buscar si el producto ya está en el carrito
		DetalleOrden existente = detalles.stream().filter(p -> p.getProducto().getId() == id).findFirst().orElse(null);

		if (existente != null) {
			// Si el producto ya existe en el carrito, actualiza la cantidad y el total
			existente.setCantidad(existente.getCantidad() + cantidad);
			existente.setTotal(existente.getCantidad() * existente.getProducto().getPrecio());
		} else {
			// Si el producto no existe en el carrito, añádelo
			detalleOrden.setCantidad(cantidad);
			detalleOrden.setPrecio(producto.getPrecio());
			detalleOrden.setNombre(producto.getNombre());
			detalleOrden.setTotal(producto.getPrecio() * cantidad);
			detalleOrden.setProducto(producto);

			detalles.add(detalleOrden);
		}

		// Calcular el subtotal del carrito
		sumatotal = detalles.stream().mapToDouble(DetalleOrden::getTotal).sum();
		orden.setTotal(sumatotal);

		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);

		return "usuario/carrito";
	}

	// quitar producto del carrtio
	@GetMapping("/delete/cart/{id}")
	public String eliminarproducart(@PathVariable Integer id, Model model) {

		List<DetalleOrden> ordenesNueva = new ArrayList<DetalleOrden>();
		for (DetalleOrden detalleOrden : detalles) {
			if (detalleOrden.getProducto().getId() != id) {
				ordenesNueva.add(detalleOrden);
			}
		}
		// nueva lista con los productos que quedaron
		detalles = ordenesNueva;
		double sumatotal = 0;
		sumatotal = detalles.stream().mapToDouble(DetalleOrden::getTotal).sum();

		orden.setTotal(sumatotal);
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		return "usuario/carrito";
	}

	@GetMapping("/getCart")
	public String getCart(Model model) {
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		return "/usuario/carrito";
	}
	
	@GetMapping("/order")
	public String order() {
		return "usuario/resumenorden";
	}

}
