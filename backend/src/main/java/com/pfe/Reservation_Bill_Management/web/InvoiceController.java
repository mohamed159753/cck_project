package com.pfe.Reservation_Bill_Management.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.layout.element.Table;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.styledxmlparser.css.media.MediaType;
import com.pfe.Reservation_Bill_Management.entities.Invoice;
import com.pfe.Reservation_Bill_Management.services.user.InvoiceService;
import com.pfe.Reservation_Bill_Management.services.user.PaymentService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/invoices")
@CrossOrigin(origins = "http://localhost:4200") // Adjust this to your Angular app URL
//@CrossOrigin(origins = "*") // Allow Angular requests
public class InvoiceController {
	    @Autowired
	    private InvoiceService invoiceService;

	    @GetMapping()
	    public List<Invoice> getAllInvoices() {
	        return invoiceService.getAllInvoices();
	    }
	    
	    @GetMapping("/{invoiceId}")
	    public Optional<Invoice> getInvoiceById(@PathVariable("invoiceId") Long invoiceId) {
	        return Optional.of(invoiceService.getInvoiceById(invoiceId));
	    }
	    
	    @GetMapping("/university/{universityId}")
	    public ResponseEntity<List<Invoice>> getInvoices(@PathVariable String universityId) {
	        try {
	            List<Invoice> invoices = invoiceService.getInvoicesForUniversity(universityId);
	            return ResponseEntity.ok(invoices);
	        } catch (IllegalArgumentException e) {
	            return ResponseEntity.notFound().build();
	        }
	    }
	    
	    @GetMapping("/month/{month}")
	    public List<Invoice> getInvoicesByMonth(@PathVariable String month) {
	        YearMonth ym = YearMonth.parse(month); // e.g., "2024-09"
	        return invoiceService.getInvoicesByMonth(month);
	    }

	    @GetMapping("/university/{universityId}/month/{month}")
	    public List<Invoice> getInvoicesByUniversityAndMonth(@PathVariable String universityId,
	                                                         @PathVariable String month) {
	        YearMonth ym = YearMonth.parse(month);
	        return invoiceService.getInvoicesByUniversityAndMonth(universityId, ym);
	    }
	    
	    @GetMapping("/export/{month}")
	    public void exportInvoicesToCSV(@PathVariable String month, HttpServletResponse response) throws IOException {
	        YearMonth ym = YearMonth.parse(month);
	        List<Invoice> invoices = invoiceService.getInvoicesByMonth(ym.toString());

	        response.setContentType("text/csv");
	        response.setHeader("Content-Disposition", "attachment; filename=invoices-" + month + ".csv");

	        PrintWriter writer = response.getWriter();
	        writer.println("University,Month,FixedCost,PAYG,Total");

	        for (Invoice invoice : invoices) {
	            writer.printf("%s,%s,%.2f,%.2f,%.2f\n",
	                invoice.getUniversity().getUniversityName(),
	                invoice.getMonth(),
	                invoice.getFixedAmount(),
	                invoice.getPaygTotal(),
	                invoice.getTotalAmount()
	            );
	        }

	        writer.flush();
	        writer.close();
	    }
	    
	    @GetMapping("/export/")
	    public void exportAllInvoicesToCSV(HttpServletResponse response) throws IOException {
	        List<Invoice> invoices = invoiceService.getAllInvoices();

	        response.setContentType("text/csv");
	        response.setHeader("Content-Disposition", "attachment; filename=invoices-" + ".csv");

	        PrintWriter writer = response.getWriter();
	        writer.println("University,Month,FixedCost,PAYG,Total");

	        for (Invoice invoice : invoices) {
	            writer.printf("%s,%s,%.2f,%.2f,%.2f\n",
	                invoice.getUniversity().getUniversityName(),
	                invoice.getMonth(),
	                invoice.getFixedAmount(),
	                invoice.getPaygTotal(),
	                invoice.getTotalAmount()
	            );
	        }

	        writer.flush();
	        writer.close();
	    }
	    
	    @GetMapping("/{invoiceId}/download/pdf")
	    public void downloadInvoicePdf(
	        @PathVariable Long invoiceId,
	        HttpServletResponse response
	    ) {
	        try {
	            Invoice invoice = invoiceService.getInvoiceById(invoiceId);
	            if (invoice == null) {
	                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	                return;
	            }

	            ByteArrayOutputStream baos = new ByteArrayOutputStream();
	            PdfWriter writer = new PdfWriter(baos);
	            PdfDocument pdfDoc = new PdfDocument(writer);
	            Document document = new Document(pdfDoc);

	            // === Add Logo ===
	            String logoPath = "src/main/resources/static/logo.png"; // Adjust path if needed
	            ImageData imageData = ImageDataFactory.create(logoPath);
	            Image logo = new Image(imageData).scaleToFit(100, 100).setHorizontalAlignment(HorizontalAlignment.CENTER);
	            document.add(logo);

	            // === Title ===
	            document.add(new Paragraph("University Invoice")
	                .setTextAlignment(TextAlignment.CENTER)
	                .setFontSize(20)
	                .setBold()
	                .setMarginBottom(20));

	            // === Add University Info Table ===
	            Table table = new Table(2);
	            table.setWidth(UnitValue.createPercentValue(100));

	            table.addCell(new Cell().add(new Paragraph("University Name").setBold()));
	            table.addCell(new Cell().add(new Paragraph(invoice.getUniversity().getUniversityName())));

	            table.addCell(new Cell().add(new Paragraph("Month").setBold()));
	            table.addCell(new Cell().add(new Paragraph(invoice.getMonth().toString())));

	            table.addCell(new Cell().add(new Paragraph("Fixed Amount").setBold()));
	            table.addCell(new Cell().add(new Paragraph("$" + invoice.getFixedAmount())));

	            table.addCell(new Cell().add(new Paragraph("PAYG Total").setBold()));
	            table.addCell(new Cell().add(new Paragraph("$" + invoice.getPaygTotal())));

	            table.addCell(new Cell().add(new Paragraph("Total Amount").setBold()));
	            table.addCell(new Cell().add(new Paragraph("$" + invoice.getTotalAmount())));

	            table.addCell(new Cell().add(new Paragraph("Status").setBold()));
	            table.addCell(new Cell().add(new Paragraph(invoice.getStatus())));

	            table.addCell(new Cell().add(new Paragraph("Issue Date").setBold()));
	            table.addCell(new Cell().add(new Paragraph(invoice.getIssueDate().toString())));

	            table.addCell(new Cell().add(new Paragraph("Due Date").setBold()));
	            table.addCell(new Cell().add(new Paragraph(invoice.getDueDate().toString())));

	            document.add(table);

	            // Optional: Add some footer or note
	            document.add(new Paragraph("\nOk Maysem 22525521")
	                .setTextAlignment(TextAlignment.CENTER)
	                .setFontSize(10)
	                .setItalic());

	            document.close();

	            // === Set Headers for Download ===
	            response.setContentType("application/pdf");
	            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice-" + invoice.getMonth() + ".pdf");
	            response.getOutputStream().write(baos.toByteArray());
	            response.getOutputStream().flush();

	        } catch (Exception e) {
	            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	            throw new RuntimeException("Failed to generate PDF invoice", e);
	        }
	    }
	

	}
