package br.gov.sp.etec.gestaomicroprodutores.api.infra.validation;

import br.gov.sp.etec.gestaomicroprodutores.api.infra.exception.RegraDeNegocioException;
import java.util.Base64;

public final class ImagemCadastroValidator {
    private static final int LIMITE_BYTES = 300_000;
    private ImagemCadastroValidator() {}

    public static void validar(String imagem) {
        if (imagem == null || imagem.isBlank()) return;
        if (imagem.length() > 420_000) throw new RegraDeNegocioException("Imagem muito grande. Limite de 300 KB.");
        String prefixo;
        if (imagem.startsWith("data:image/png;base64,")) prefixo = "png";
        else if (imagem.startsWith("data:image/jpeg;base64,")) prefixo = "jpeg";
        else if (imagem.startsWith("data:image/webp;base64,")) prefixo = "webp";
        else throw new RegraDeNegocioException("Escolha uma imagem PNG, JPEG ou WEBP.");

        try {
            byte[] bytes = Base64.getDecoder().decode(imagem.substring(imagem.indexOf(',') + 1));
            if (bytes.length == 0 || bytes.length > LIMITE_BYTES) throw new IllegalArgumentException();
            boolean png = bytes.length >= 8 && (bytes[0] & 255) == 137 && bytes[1] == 80 && bytes[2] == 78 && bytes[3] == 71;
            boolean jpg = bytes.length >= 3 && (bytes[0] & 255) == 255 && (bytes[1] & 255) == 216 && (bytes[2] & 255) == 255;
            boolean webp = bytes.length >= 12 && bytes[0] == 82 && bytes[1] == 73 && bytes[2] == 70 && bytes[3] == 70
                    && bytes[8] == 87 && bytes[9] == 69 && bytes[10] == 66 && bytes[11] == 80;
            if (!(prefixo.equals("png") && png || prefixo.equals("jpeg") && jpg || prefixo.equals("webp") && webp)) {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException ex) {
            throw new RegraDeNegocioException("Imagem inválida ou acima do limite de 300 KB.");
        }
    }
}
