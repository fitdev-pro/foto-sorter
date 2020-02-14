package Infrastructure;

import Application.IRepository;

public class DependencyContainer {
    private static DependencyContainer instance;

    private IRepository repository;
    private ServiceDispatcher serviceDispatcher;

    public static DependencyContainer getInstance(){
        if(instance == null){
            instance = new DependencyContainer();
        }

        return instance;
    }

    private DependencyContainer() {
        this.repository = new GalleryRepository();
        this.serviceDispatcher = new ServiceDispatcher(this.repository);
    }

    public ServiceDispatcher getServiceDispatcher(){
        return serviceDispatcher;
    }

    public void initService
}
