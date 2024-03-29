package com.noithat.WebNoiThat.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.noithat.WebNoiThat.dao.ItemDao;
import com.noithat.WebNoiThat.entity.Item;
import com.noithat.WebNoiThat.entity.Order;
import com.noithat.WebNoiThat.entity.Product;
import com.noithat.WebNoiThat.model.ItemDTO;
import com.noithat.WebNoiThat.model.OrderDTO;
import com.noithat.WebNoiThat.model.ProductDTO;
import com.noithat.WebNoiThat.model.SaleDTO;
import com.noithat.WebNoiThat.service.ItemService;

@Service
@Transactional
public class ItemServiceImpl implements ItemService {

	@Autowired
	private ItemDao itemDao;
	
	@Override
	public void insert(ItemDTO itemDTO) {
		
		Product product = new Product();
		product.setProductId(itemDTO.getProductDTO().getProductId());
		Order order = new Order();
		order.setOrderId(itemDTO.getOrderDTO().getOrderId());
		
		Item item = new Item();
		item.setItemId(itemDTO.getItemId());
		item.setProduct(product);
		item.setUnitPrice(itemDTO.getUnitPrice());
		item.setQuantity(itemDTO.getQuantity());
		item.setOrder(order);
		
		itemDao.insert(item);
	}

	@Override
	public void update(ItemDTO itemDTO) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(long itemId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<ItemDTO> findAll(int pageIndex, int pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ItemDTO> findByOrderId(long orderId) {
		List<Item> items = itemDao.findByOrderId(orderId);
		List<ItemDTO> itemDTOs = new ArrayList<>();
		for (Item item : items) {
			
			OrderDTO orderDTO = new OrderDTO();
			orderDTO.setOrderId(item.getOrder().getOrderId());
			
			SaleDTO saleDTO = new SaleDTO();
			saleDTO.setSaleId(item.getProduct().getSale().getSaleId());
			saleDTO.setSalePercent(item.getProduct().getSale().getSalePercent());
			
			ProductDTO productDTO = new ProductDTO();
			productDTO.setProductId(item.getProduct().getProductId());
			productDTO.setImage(item.getProduct().getImage());
			productDTO.setProductName(item.getProduct().getProductName());
			productDTO.setPrice(item.getProduct().getPrice());
			productDTO.setSaleDTO(saleDTO);
			
			ItemDTO itemDTO = new ItemDTO();
			itemDTO.setItemId(item.getItemId());
			itemDTO.setOrderDTO(orderDTO);
			itemDTO.setProductDTO(productDTO);
			itemDTO.setQuantity(item.getQuantity());
			itemDTO.setUnitPrice(item.getUnitPrice());
			
			itemDTOs.add(itemDTO);
		}
		return itemDTOs;
	}

}
