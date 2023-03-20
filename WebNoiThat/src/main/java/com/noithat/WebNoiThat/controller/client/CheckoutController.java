package com.noithat.WebNoiThat.controller.client;

import java.sql.Date;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.noithat.WebNoiThat.dao.ItemDao;
import com.noithat.WebNoiThat.dao.OrderDao;
import com.noithat.WebNoiThat.entity.Item;
import com.noithat.WebNoiThat.entity.Order;
import com.noithat.WebNoiThat.entity.Product;
import com.noithat.WebNoiThat.entity.User;
import com.noithat.WebNoiThat.model.ItemDTO;
import com.noithat.WebNoiThat.model.UserPrincipal;

@Controller
@RequestMapping(value = "/client")
public class CheckoutController {

	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	private ItemDao itemDao;
	
	@PostMapping(value = "/checkout")
	public String checkout(HttpSession session) {
		
		float subTotal = 0; 
		float fee = 5; 
		
		UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		User user = new User();
		user.setUserId(userPrincipal.getUserId());
		
        Date date = new Date(new java.util.Date().getTime()); 
        
		Object object = session.getAttribute("cart"); 
		Map<Long, ItemDTO> mapItem = (Map<Long, ItemDTO>) object;
		
		for(Map.Entry<Long, ItemDTO> entry : mapItem.entrySet()) {
		    Long key = entry.getKey();
		    ItemDTO value = entry.getValue();
		    
		    subTotal += (value.getUnitPrice() * value.getQuantity()); 
		}
		
		Order order = new Order();
		order.setBuyDate(date);
		order.setBuyer(user);
		order.setStatus("PENDING");
		order.setPriceTotal(subTotal + fee);
		
		orderDao.insert(order);
		
		
		for(Map.Entry<Long, ItemDTO> entry : mapItem.entrySet()) {
		    Long key = entry.getKey();
		    ItemDTO value = entry.getValue();
		    
		    Product product = new Product();
		    product.setProductId(entry.getValue().getProductDTO().getProductId());
		    
		    Item item = new Item();
		    item.setItemId(entry.getValue().getItemId());
		    item.setProduct(product);
		    item.setQuantity(entry.getValue().getQuantity());
		    item.setUnitPrice(entry.getValue().getUnitPrice());
		    item.setOrder(order);
		    
		    
			itemDao.insert(item);
		}
	
		mapItem.clear();
		session.setAttribute("cart", mapItem);
		
		return "redirect:/client/my-order";
	}
}
