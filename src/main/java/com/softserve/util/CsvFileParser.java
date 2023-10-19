package com.softserve.util;

import com.opencsv.bean.CsvToBeanBuilder;
import com.softserve.dto.StudentImportDTO;
import com.softserve.dto.TeacherImportDTO;
import com.softserve.exception.ParseFileException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public final class CsvFileParser {

    private CsvFileParser() {
    }

    public static List<StudentImportDTO> getStudentsFromFile(MultipartFile file) {
        String fileName = String.join("", "students_",
                String.valueOf(LocalDateTime.now().getNano()), ".csv");

        File csvFile = new File(fileName);

        List<StudentImportDTO> students = new ArrayList<>();
        try {
            file.transferTo(csvFile);

            try (Reader reader = new FileReader(csvFile, StandardCharsets.UTF_8)) {
                students = new CsvToBeanBuilder<StudentImportDTO>(reader)
                        .withType(StudentImportDTO.class)
                        .build().parse();
            }
        } catch (RuntimeException e) {
            log.error("Error occurred while parsing file {}", csvFile.getName(), e);
            throw new ParseFileException("Bad file format");
        } catch (IOException e) {
            log.error("Error occurred while accessing to file {}", csvFile.getName(), e);
        } finally {
            fileDelete(csvFile);
        }
        return students;
    }

    private static void fileDelete(File file) {
        try {
            Files.delete(file.toPath());
        } catch (IOException e) {
            log.error("Error occurred while delete file {}", file.getName(), e);
        }
    }

    public static List<TeacherImportDTO> getTeachersFromFile(MultipartFile file) {
        String fileName = String.join("", "teachers_",
                String.valueOf(LocalDateTime.now().getNano()), ".csv");

        File csvFile = new File(fileName);

        List<TeacherImportDTO> teachers = new ArrayList<>();
        try {
            file.transferTo(csvFile);

            try (Reader reader = new FileReader(csvFile, StandardCharsets.UTF_8)) {
                teachers = new CsvToBeanBuilder<TeacherImportDTO>(reader)
                        .withType(TeacherImportDTO.class)
                        .build().parse();
            }
        } catch (RuntimeException e) {
            log.error("Error occurred while parsing file {}", csvFile.getName(), e);
            throw new ParseFileException("Bad file format");
        } catch (IOException e) {
            log.error("Error occurred while accessing to file {}", csvFile.getName(), e);
        } finally {
            fileDelete(csvFile);
        }
        return teachers;
    }
}
