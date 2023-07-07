package hexlet.code.app.service;

import hexlet.code.app.dto.LabelDto;
import hexlet.code.app.model.Label;
import hexlet.code.app.repository.LabelRepository;

import jakarta.transaction.Transactional;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@Transactional
@AllArgsConstructor
public class LabelServiceImpl implements LabelService {
    private final LabelRepository labelRepository;

    @Override
    public Label getLabel(long id) {
        return labelRepository.findById(id).get();
    }

    @Override
    public Iterable<Label> getLabels() {
        return labelRepository.findAll();
    }

    @Override
    public Label createLabel(LabelDto labelDto) {
        final Label newLabel = new Label();

        newLabel.setName(labelDto.getName());

        return labelRepository.save(newLabel);
    }

    @Override
    public Label updateLabel(long id, LabelDto labelDto) {
        final Label labelToUpdate = labelRepository.findById(id).get();

        labelToUpdate.setName(labelDto.getName());

        return labelRepository.save(labelToUpdate);
    }

    @Override
    public void deleteLabel(long id) {
        final Label labelToDelete = labelRepository.findById(id).get();

        labelRepository.delete(labelToDelete);
    }
}
