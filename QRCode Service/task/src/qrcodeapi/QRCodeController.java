package qrcodeapi;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

@RestController
public class QRCodeController {
    private static final int MIN_SIZE = 150;
    private static final int MAX_SIZE = 350;
    private static final String BAD_SIZE_MSG = "Image size must be between 150 and 350 pixels";
    private static final String BAD_TYPE_MSG = "Only png, jpeg and gif image types are supported";
    private static final String BAD_CORRECTION_MSG = "Permitted error correction levels are L, M, Q, H";
    private static final String BAD_CONTENTS_MSG = "Contents cannot be null or blank";
    private static final Map<String, MediaType> SUPPORTED_TYPES = Map.of(
            "png", MediaType.IMAGE_PNG,
            "jpeg", MediaType.IMAGE_JPEG,
            "gif", MediaType.parseMediaType("image/gif")
    );
    private static final Map<String, ErrorCorrectionLevel> SUPPORTED_CORRECTIONS = Map.of(
            "L", ErrorCorrectionLevel.L,
            "M", ErrorCorrectionLevel.M,
            "Q", ErrorCorrectionLevel.Q,
            "H", ErrorCorrectionLevel.H
    );
    private final QRCodeWriter qrCodeWriter = new QRCodeWriter();

    @GetMapping("/api/health")
    public ResponseEntity<Void> health() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/qrcode")
    public ResponseEntity<?> getQrCode(
            @RequestParam(required = false) String contents,
            @RequestParam(defaultValue = "250") int size,
            @RequestParam(defaultValue = "L") String correction,
            @RequestParam(defaultValue = "png") String type
    ) {
        if (contents == null || contents.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", BAD_CONTENTS_MSG));
        }

        if (size < MIN_SIZE || size > MAX_SIZE) {
            return ResponseEntity.badRequest().body(Map.of("error", BAD_SIZE_MSG));
        }

        ErrorCorrectionLevel correctionLevel = SUPPORTED_CORRECTIONS.get(correction);
        if (correctionLevel == null) {
            return ResponseEntity.badRequest().body(Map.of("error", BAD_CORRECTION_MSG));
        }

        MediaType mediaType = SUPPORTED_TYPES.get(type);
        if (mediaType == null) {
            return ResponseEntity.badRequest().body(Map.of("error", BAD_TYPE_MSG));
        }

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Map<EncodeHintType, ?> hints = Map.of(EncodeHintType.ERROR_CORRECTION, correctionLevel);
            BitMatrix matrix = qrCodeWriter.encode(contents, BarcodeFormat.QR_CODE, size, size, hints);
            BufferedImage image = MatrixToImageWriter.toBufferedImage(matrix);
            ImageIO.write(image, type, outputStream);
            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .body(outputStream.toByteArray());
        } catch (IOException | WriterException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
