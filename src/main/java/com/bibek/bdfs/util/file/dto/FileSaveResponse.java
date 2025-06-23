package com.bibek.bdfs.util.file.dto;

import com.bibek.bdfs.util.file.FileType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileSaveResponse {
    private String fileName;
    private String fileDownloadUri;
    private FileType fileType;
    private String fileDimension;
}
