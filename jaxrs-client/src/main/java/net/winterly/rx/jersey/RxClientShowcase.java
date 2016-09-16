package net.winterly.rx.jersey;

import org.glassfish.jersey.client.rx.RxClient;
import org.glassfish.jersey.client.rx.RxWebTarget;
import org.glassfish.jersey.client.rx.rxjava.RxObservable;
import org.glassfish.jersey.client.rx.rxjava.RxObservableInvoker;
import rx.Observable;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

public class RxClientShowcase {

    public static void main(String[] args) {

        RxClient<RxObservableInvoker> rxClient = RxObservable.newClient();
        rxClient.register(RxBodyReader.class);

        RxWebTarget<RxObservableInvoker> rxTarget = rxClient.target("http://localhost:8081");

        Resource resource = WebResourceFactoryPatched.newResource(Resource.class, rxTarget);
    }

    @Path("/")
    interface Resource {
        @GET
        Observable<String> get();
    }
}
