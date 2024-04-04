package com.example.restaurantsapp.controller;

import com.example.restaurantsapp.dto.OrderDTO;
import com.example.restaurantsapp.dto.ResponseDTO;
import com.example.restaurantsapp.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/cretate")
    public ResponseEntity<List<ResponseDTO<OrderDTO>>> cretate(@RequestBody OrderDTO orderDTO) {
        List<ResponseDTO<OrderDTO>> responseDTOS = orderService.create(orderDTO);
        return ResponseEntity.ok().body(responseDTOS);
    }


    @GetMapping("/getAllOrderList")
    public ResponseEntity<List<ResponseDTO<OrderDTO>>> getAllOrderList() {
        List<ResponseDTO<OrderDTO>> list = orderService.getList();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/getAllOrderListByStatusNew")
    public ResponseEntity<List<ResponseDTO<OrderDTO>>> getAllOrderListByStatusNew() {
        List<ResponseDTO<OrderDTO>> list = orderService.getAllOrderListByStatusNew();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/getAllOrderListByStatusPrepered")
    public ResponseEntity<List<ResponseDTO<OrderDTO>>> getAllOrderListByStatusPrepered() {
        List<ResponseDTO<OrderDTO>> list = orderService.getAllOrderListByStatusPrepered();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/getAllOrderListByStatusFinshed")
    public ResponseEntity<List<ResponseDTO<OrderDTO>>> getAllOrderListByStatusFinshed() {
        List<ResponseDTO<OrderDTO>> list = orderService.getAllOrderListByStatusFinshed();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/getOrder/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<OrderDTO>> getOrder(@PathVariable("id") Integer id) {
        ResponseDTO<OrderDTO> order = orderService.getOrder(id);
        return ResponseEntity.ok().body(order);
    }


    @PutMapping("/updateStatusPepred/{id}")
    private ResponseEntity<OrderDTO> updateStatusPREPERATED(@PathVariable("id") Integer id) {
        ResponseDTO<OrderDTO> orderDTOResponseDTO = orderService.changeStatusPREPERATED(id);
        return ResponseEntity.ok().body(orderDTOResponseDTO.getData());
    }


    @PutMapping("/updateStatusFinished/{id}")
    private ResponseEntity<OrderDTO> updateStatusFINESHED(@PathVariable("id") Integer id) {
        ResponseDTO<OrderDTO> orderDTOResponseDTO = orderService.changeStatusFINESHED(id);
        return ResponseEntity.ok().body(orderDTOResponseDTO.getData());
    }

    @DeleteMapping("/delete/{id}")
    private ResponseEntity<ResponseDTO<OrderDTO>> delete(@PathVariable("id") Integer id) {
        ResponseDTO delete = orderService.delete(id);
        return ResponseEntity.ok().body(delete);
    }


}
