package com.airbyte.dorm.responsefile;

import com.airbyte.dorm.common.ParentService;
import com.airbyte.dorm.dto.ResponseFileDTO;
import com.airbyte.dorm.files.FileStorageService;
import com.airbyte.dorm.model.FileDB;
import com.airbyte.dorm.model.ResponseFile;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class ResponseFileService extends ParentService<ResponseFile, ResponseFileRepository, ResponseFileDTO> {
    private final FileStorageService fileStorageService;

    public ResponseFileService(ResponseFileRepository repository, FileStorageService fileStorageService) {
        super(repository);
        this.fileStorageService = fileStorageService;
    }

    @Override
    public ResponseFile updateModelFromDto(ResponseFile model, ResponseFileDTO dto) {
        return null;
    }

    @Override
    public ResponseFile convertDTO(ResponseFileDTO dto) {
        FileDB fileDB = fileStorageService.getFile(dto.getFileId());

        ResponseFile responseFile = new ResponseFile();
        responseFile.setFileId(fileDB.getId());
        responseFile.setType(fileDB.getType().split("/")[1]);
        responseFile.setFullType(fileDB.getType());
        responseFile.setParentId(dto.getParentId());
        responseFile.setParentType(dto.getParentType());
        responseFile.setName(dto.getName());
        responseFile.setOriginalName(fileDB.getName());
        return responseFile;
    }

    @Override
    public List<ResponseFile> getWithSearch(ResponseFileDTO search) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ResponseFile> criteriaBuilderQuery = criteriaBuilder.createQuery(ResponseFile.class);

        Root<ResponseFile> RecordRoot = criteriaBuilderQuery.from(ResponseFile.class);
        List<Predicate> predicates = new ArrayList<>();

        if (search.getParentId() != null) {
            predicates.add(criteriaBuilder.equal(RecordRoot.get("parentId"), search.getParentId()));
        }
        if (search.getParentType() != null) {
            predicates.add(criteriaBuilder.equal(RecordRoot.get("parentType"), search.getParentType()));
        }
        if (search.getFileId() != null) {
            predicates.add(criteriaBuilder.equal(RecordRoot.get("fileId"), search.getFileId()));
        }

        criteriaBuilderQuery.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(criteriaBuilderQuery).getResultList();
    }
}
