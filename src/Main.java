import java.io.*;
import java.nio.file.*;
import java.util.Scanner;
import java.util.zip.*;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите путь к папке, которую нужно заархивировать: ");
        String sourceFolderPath = scanner.nextLine().trim();
        System.out.print("Введите путь и имя ZIP-файла (например, C:\\Users\\User\\Documents\\archive.zip): ");
        String zipFilePath = scanner.nextLine().trim();
        scanner.close();
        try {
            zipFolder(Paths.get(sourceFolderPath), Paths.get(zipFilePath));
            System.out.println("Папка успешно заархивирована: " + zipFilePath);
        } catch (IOException e) {
            System.err.println("Ошибка при архивации: " + e.getMessage());
        }
    }
    public static void zipFolder(Path sourceFolderPath, Path zipPath) throws IOException {
        if (!Files.exists(sourceFolderPath) || !Files.isDirectory(sourceFolderPath)) {
            throw new IOException("Указанный путь не является существующей папкой.");
        }

        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipPath.toFile()))) {
            Files.walk(sourceFolderPath).filter(path -> !Files.isDirectory(path)).forEach(path -> {
                        String zipEntryName = sourceFolderPath.relativize(path).toString().replace("\\", "/");
                        try {
                            zos.putNextEntry(new ZipEntry(zipEntryName));
                            Files.copy(path, zos);
                            zos.closeEntry();
                        } catch (IOException e) {
                            System.err.println("Ошибка при добавлении файла в архив: " + path + " - " + e.getMessage());
                        }
                    });
        }
    }
}
