package com.apeiron.paperlabs.service.impl;

import com.apeiron.paperlabs.domain.DowloadHistory;
import com.apeiron.paperlabs.service.FileDownloadService;
import com.apeiron.paperlabs.service.OrderService;
import com.apeiron.paperlabs.service.dto.DowloadHistoryDTO;
import com.apeiron.paperlabs.service.dto.OrderDTO;
import com.apeiron.paperlabs.service.mapper.OrderMapper;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * class implements the FileDownloadService for downloading the generated documents in a PDF format.
 *
 * @author Mohamed Belhassen Zinelabidine
 * @version %I%, %G%
 * @since 0.1-SNAPSHOT
 */
@Service
public class FileDownloadServiceImp implements FileDownloadService {

    private OrderService orderService;

    public FileDownloadServiceImp(OrderService orderService) {
        this.orderService = orderService;
    }
    /**
     * @param filePath filePath
     * @param response Http response.
     * @return file from system.
     */
    public Resource downloadPdfFile(String filePath, String orderId, HttpServletResponse response) {
        response.setContentType("application/pdf; charset=utf-8");
        Optional<OrderDTO> order = this.orderService.findOne(orderId);
        List<DowloadHistoryDTO> downloadHistories;
        DowloadHistoryDTO downloadHistory = new DowloadHistoryDTO();
        downloadHistory.setDate(Instant.now());
        if(order.isPresent()) {
            downloadHistories = order.get().getDowloadHistories();
            downloadHistories.add(downloadHistory);
            order.get().setDowloadHistories(downloadHistories);
            this.orderService.save(order.get());
        }
        return new FileSystemResource(filePath);
    }
}
