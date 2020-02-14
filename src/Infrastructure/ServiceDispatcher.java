package Infrastructure;

import Application.IRepository;
import Application.IService;

public class ServiceDispatcher {
    private IRepository repository;

    public ServiceDispatcher(IRepository repository) {
        this.repository = repository;
    }

    public void dispatch(IService service){
        service.init(this.repository);
        service.invoke();
    }
}
