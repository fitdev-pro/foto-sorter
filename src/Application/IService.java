package Application;

import Domain.FileResult;

import java.util.Collection;

public interface IService {
    public Collection<FileResult> invoke(IRepository repository);
}
