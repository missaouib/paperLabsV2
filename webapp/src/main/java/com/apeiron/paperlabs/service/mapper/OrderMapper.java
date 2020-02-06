package com.apeiron.paperlabs.service.mapper;

import com.apeiron.paperlabs.domain.DowloadHistory;
import com.apeiron.paperlabs.domain.GeneratedLegalDocument;
import com.apeiron.paperlabs.domain.Order;
import com.apeiron.paperlabs.service.LegalDocumentService;
import com.apeiron.paperlabs.service.UserService;
import com.apeiron.paperlabs.service.dto.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * Mapper for the entity {@link Order} and its DTO {@link OrderDTO}.
 */
@Service
public class OrderMapper  {

    private final LegalDocumentService legalDocumentService;

    private final UserService userService;

    public OrderMapper(
        LegalDocumentService legalDocumentService,
        UserService userService
    ) {
        this.legalDocumentService = legalDocumentService;
        this.userService = userService;
    }

    public Order toEntity(OrderDTO dto) {
        if (dto == null) {
            return null;
        } else {
            Order order = new Order();
            order.setId(dto.getId());
            order.setOrderIdentifier(dto.getOrderIdentifier());
            order.setPaymentMethod(dto.getPaymentMethod());
            order.setTotalPrice(dto.getTotalPrice());
            order.setPrice(dto.getPrice());
            order.setTax(dto.getTax());
            order.setInvoiceFilePath(dto.getInvoiceFilePath());
            order.setStatus(dto.getStatus());
            order.setDestinationEmail(dto.getDestinationEmail());
            order.setDowloadHistories(this.dowloadHistoryDTOListToDowloadHistoryList(dto.getDowloadHistories()));
            order.setGeneratedLegalDocument(this.generatedLegalDocumentDTOToGeneratedLegalDocument(dto.getGeneratedLegalDocument()));
            if (dto.getLegalDocument() != null) {
                order.setLegalDocumentId(dto.getLegalDocument().getId());
            }
            order.setUserId(dto.getUser().getEmail());
            return order;
        }
    }

    public OrderDTO toDto(Order entity) {
        if (entity == null) {
            return null;
        } else {
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setId(entity.getId());
            orderDTO.setOrderIdentifier(entity.getOrderIdentifier());
            orderDTO.setPaymentMethod(entity.getPaymentMethod());
            orderDTO.setTotalPrice(entity.getTotalPrice());
            orderDTO.setPrice(entity.getPrice());
            orderDTO.setTax(entity.getTax());
            orderDTO.setInvoiceFilePath(entity.getInvoiceFilePath());
            orderDTO.setStatus(entity.getStatus());
            orderDTO.setDestinationEmail(entity.getDestinationEmail());
            orderDTO.setDowloadHistories(this.dowloadHistoryListToDowloadHistoryDTOList(entity.getDowloadHistories()));
            orderDTO.setGeneratedLegalDocument(this.generatedLegalDocumentToGeneratedLegalDocumentDTO(entity.getGeneratedLegalDocument()));
            if (entity.getLegalDocumentId() != null) {
                Optional<LegalDocumentDTO> one = legalDocumentService.findOne(entity.getLegalDocumentId());

                if (one.isPresent()) {
                    orderDTO.setLegalDocument(one.get());
                }

            }
            if (entity.getUserId() != null) {
                Optional<UserDTO> one = userService.getUserWithAuthoritiesByLogin(entity.getUserId());

                if (one.isPresent()) {
                    orderDTO.setUser(one.get());
                }

            }
            return orderDTO;

        }
    }

    public List<Order> toEntity(List<OrderDTO> dtoList) {
        if (dtoList == null) {
            return null;
        } else {
            List<Order> list = new ArrayList(dtoList.size());
            Iterator var3 = dtoList.iterator();

            while(var3.hasNext()) {
                OrderDTO orderDTO = (OrderDTO)var3.next();
                list.add(this.toEntity(orderDTO));
            }

            return list;
        }
    }

    public List<OrderDTO> toDto(List<Order> entityList) {
        if (entityList == null) {
            return null;
        } else {
            List<OrderDTO> list = new ArrayList(entityList.size());
            Iterator var3 = entityList.iterator();

            while(var3.hasNext()) {
                Order order = (Order)var3.next();
                list.add(this.toDto(order));
            }

            return list;
        }
    }

    protected DowloadHistory dowloadHistoryDTOToDowloadHistory(DowloadHistoryDTO dowloadHistoryDTO) {
        if (dowloadHistoryDTO == null) {
            return null;
        } else {
            DowloadHistory dowloadHistory = new DowloadHistory();
            dowloadHistory.setId(dowloadHistoryDTO.getId());
            dowloadHistory.setDate(dowloadHistoryDTO.getDate());
            return dowloadHistory;
        }
    }

    protected List<DowloadHistory> dowloadHistoryDTOListToDowloadHistoryList(List<DowloadHistoryDTO> list) {
        if (list == null) {
            return null;
        } else {
            List<DowloadHistory> list1 = new ArrayList(list.size());
            Iterator var3 = list.iterator();

            while(var3.hasNext()) {
                DowloadHistoryDTO dowloadHistoryDTO = (DowloadHistoryDTO)var3.next();
                list1.add(this.dowloadHistoryDTOToDowloadHistory(dowloadHistoryDTO));
            }

            return list1;
        }
    }

    protected GeneratedLegalDocument generatedLegalDocumentDTOToGeneratedLegalDocument(GeneratedLegalDocumentDTO generatedLegalDocumentDTO) {
        if (generatedLegalDocumentDTO == null) {
            return null;
        } else {
            GeneratedLegalDocument generatedLegalDocument = new GeneratedLegalDocument();
            generatedLegalDocument.setId(generatedLegalDocumentDTO.getId());
            generatedLegalDocument.setGeneratedWordFilePath(generatedLegalDocumentDTO.getGeneratedWordFilePath());
            generatedLegalDocument.setGenratedPDFFilePath(generatedLegalDocumentDTO.getGenratedPDFFilePath());
            generatedLegalDocument.setDate(generatedLegalDocumentDTO.getDate());
            generatedLegalDocument.setPaymentDate(generatedLegalDocumentDTO.getPaymentDate());
            return generatedLegalDocument;
        }
    }

    protected DowloadHistoryDTO dowloadHistoryToDowloadHistoryDTO(DowloadHistory dowloadHistory) {
        if (dowloadHistory == null) {
            return null;
        } else {
            DowloadHistoryDTO dowloadHistoryDTO = new DowloadHistoryDTO();
            dowloadHistoryDTO.setId(dowloadHistory.getId());
            dowloadHistoryDTO.setDate(dowloadHistory.getDate());
            return dowloadHistoryDTO;
        }
    }

    protected List<DowloadHistoryDTO> dowloadHistoryListToDowloadHistoryDTOList(List<DowloadHistory> list) {
        if (list == null) {
            return null;
        } else {
            List<DowloadHistoryDTO> list1 = new ArrayList(list.size());
            Iterator var3 = list.iterator();

            while(var3.hasNext()) {
                DowloadHistory dowloadHistory = (DowloadHistory)var3.next();
                list1.add(this.dowloadHistoryToDowloadHistoryDTO(dowloadHistory));
            }

            return list1;
        }
    }

    protected GeneratedLegalDocumentDTO generatedLegalDocumentToGeneratedLegalDocumentDTO(GeneratedLegalDocument generatedLegalDocument) {
        if (generatedLegalDocument == null) {
            return null;
        } else {
            GeneratedLegalDocumentDTO generatedLegalDocumentDTO = new GeneratedLegalDocumentDTO();
            generatedLegalDocumentDTO.setId(generatedLegalDocument.getId());
            generatedLegalDocumentDTO.setGeneratedWordFilePath(generatedLegalDocument.getGeneratedWordFilePath());
            generatedLegalDocumentDTO.setGenratedPDFFilePath(generatedLegalDocument.getGenratedPDFFilePath());
            generatedLegalDocumentDTO.setDate(generatedLegalDocument.getDate());
            generatedLegalDocumentDTO.setPaymentDate(generatedLegalDocument.getPaymentDate());
            return generatedLegalDocumentDTO;
        }
    }
}
