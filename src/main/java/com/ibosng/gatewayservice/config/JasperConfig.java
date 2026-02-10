package com.ibosng.gatewayservice.config;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.InputStream;
import java.util.List;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.fonts.FontExtensionsRegistry;
import net.sf.jasperreports.engine.fonts.FontFamily;
import net.sf.jasperreports.engine.fonts.FontUtil;
import net.sf.jasperreports.extensions.DefaultExtensionsRegistry;
import net.sf.jasperreports.extensions.ExtensionsEnvironment;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class JasperConfig {
    @PostConstruct
    public void registerFonts() {
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

            String[] fontFiles = {
                    "/fonts/Trebuchet MS.ttf",
                    "/fonts/Trebuchet MS bold.ttf",
                    "/fonts/Trebuchet MS italic.ttf",
                    "/fonts/Trebuchet MS bold italic.ttf",
                    "/fonts/calibri-regular.ttf",
                    "/fonts/calibri-bold.ttf",
                    "/fonts/calibri-italic.ttf",
                    "/fonts/calibri-bold-italic.ttf",
                    "/fonts/aptos.ttf",
                    "/fonts/aptos-black.ttf",
                    "/fonts/aptos-black-italic.ttf",
                    "/fonts/aptos-bold.ttf",
                    "/fonts/aptos-extrabold.ttf",
                    "/fonts/aptos-extrabold-italic.ttf",
                    "/fonts/aptos-extrabold-italic-2.ttf",
                    "/fonts/aptos-italic.ttf",
                    "/fonts/aptos-light.ttf",
                    "/fonts/aptos-light-italic.ttf",
                    "/fonts/aptos-semibold.ttf",
                    "/fonts/Bookman Old Style Bold.ttf",
                    "/fonts/Bookman Old Style Bold Italic.ttf",
                    "/fonts/Bookman Old Style Italic.ttf",
                    "/fonts/Bookman Old Style Regular.ttf",
                    "/fonts/LHANDW.TTF",
                    "/fonts/Lucida Handwriting Italic.ttf",
                    "/fonts/lucida-handwriting-std-bold.TTF"
            };


            for (String fontFile : fontFiles) {
                InputStream fontStream = getClass().getResourceAsStream(fontFile);
                if (fontStream != null) {
                    Font font = Font.createFont(Font.TRUETYPE_FONT, fontStream);
                    ge.registerFont(font);
                    log.info("Registered custom font: {}", fontFile);
                } else {
                    log.warn("Font file not found: {}", fontFile);
                }
            }


            // Needed for debugging purposes when adding a new font and Jasper is being difficult
            /*InputStream fontStream = getClass().getResourceAsStream("/fonts/Trebuchet MS.ttf");
            if (fontStream == null) {
                log.error("Trebuchet font file not found in classpath!");
            } else {
                log.info("Trebuchet font found!");
            }

            InputStream xmlStream = getClass().getResourceAsStream("/fonts/trebuchet-fonts.xml");
            if (xmlStream == null) {
                log.error("Font XML file not found in classpath!");
            } else {
                log.info("Font XML file found successfully!");
            }


            List<FontFamily> fontFamilies = ExtensionsEnvironment.getExtensionsRegistry()
                    .getExtensions(FontFamily.class);

            System.out.println("Loaded Font Families:");
            for (FontFamily fontFamily : fontFamilies) {
                System.out.println(" - " + fontFamily.getName());
            }

            String[] fontFiles2 = {
                    "fonts/Trebuchet-MS.ttf",
                    "fonts/Trebuchet-MS-Bold.ttf",
                    "fonts/Trebuchet-MS-Italic.ttf",
                    "fonts/Trebuchet-MS-Bold-Italic.ttf"
            };

            for (String fontFile : fontFiles) {
                InputStream fontStream2 = this.getClass().getClassLoader().getResourceAsStream(fontFile);
                System.out.println(fontFile + (fontStream != null ? " ✅ FOUND" : " ❌ MISSING"));
            }*/


        } catch (Exception e) {
            log.error("Error registering fonts: {}, exception: ", e.getMessage(), e);
        }
    }
}
