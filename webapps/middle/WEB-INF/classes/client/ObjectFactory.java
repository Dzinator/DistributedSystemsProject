
package client;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the client package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _QueryRooms_QNAME = new QName("http://ws.middleRM/", "queryRooms");
    private final static QName _CommitResponse_QNAME = new QName("http://ws.middleRM/", "commitResponse");
    private final static QName _ReserveCarResponse_QNAME = new QName("http://ws.middleRM/", "reserveCarResponse");
    private final static QName _QueryCustomerInfoResponse_QNAME = new QName("http://ws.middleRM/", "queryCustomerInfoResponse");
    private final static QName _AddCarsResponse_QNAME = new QName("http://ws.middleRM/", "addCarsResponse");
    private final static QName _QueryCars_QNAME = new QName("http://ws.middleRM/", "queryCars");
    private final static QName _ReserveRoom_QNAME = new QName("http://ws.middleRM/", "reserveRoom");
    private final static QName _QueryRoomsPrice_QNAME = new QName("http://ws.middleRM/", "queryRoomsPrice");
    private final static QName _AddRooms_QNAME = new QName("http://ws.middleRM/", "addRooms");
    private final static QName _QueryCustomerInfo_QNAME = new QName("http://ws.middleRM/", "queryCustomerInfo");
    private final static QName _DeleteCars_QNAME = new QName("http://ws.middleRM/", "deleteCars");
    private final static QName _DeleteFlight_QNAME = new QName("http://ws.middleRM/", "deleteFlight");
    private final static QName _QueryCarsResponse_QNAME = new QName("http://ws.middleRM/", "queryCarsResponse");
    private final static QName _QueryFlightResponse_QNAME = new QName("http://ws.middleRM/", "queryFlightResponse");
    private final static QName _QueryCarsPriceResponse_QNAME = new QName("http://ws.middleRM/", "queryCarsPriceResponse");
    private final static QName _ReserveItinerary_QNAME = new QName("http://ws.middleRM/", "reserveItinerary");
    private final static QName _AddCars_QNAME = new QName("http://ws.middleRM/", "addCars");
    private final static QName _AddRoomsResponse_QNAME = new QName("http://ws.middleRM/", "addRoomsResponse");
    private final static QName _DeleteRooms_QNAME = new QName("http://ws.middleRM/", "deleteRooms");
    private final static QName _NewCustomerResponse_QNAME = new QName("http://ws.middleRM/", "newCustomerResponse");
    private final static QName _QueryFlight_QNAME = new QName("http://ws.middleRM/", "queryFlight");
    private final static QName _Start_QNAME = new QName("http://ws.middleRM/", "start");
    private final static QName _DeleteCustomer_QNAME = new QName("http://ws.middleRM/", "deleteCustomer");
    private final static QName _AddFlightResponse_QNAME = new QName("http://ws.middleRM/", "addFlightResponse");
    private final static QName _NewCustomerIdResponse_QNAME = new QName("http://ws.middleRM/", "newCustomerIdResponse");
    private final static QName _QueryFlightPrice_QNAME = new QName("http://ws.middleRM/", "queryFlightPrice");
    private final static QName _ReserveRoomResponse_QNAME = new QName("http://ws.middleRM/", "reserveRoomResponse");
    private final static QName _DeleteRoomsResponse_QNAME = new QName("http://ws.middleRM/", "deleteRoomsResponse");
    private final static QName _NewCustomer_QNAME = new QName("http://ws.middleRM/", "newCustomer");
    private final static QName _Startid_QNAME = new QName("http://ws.middleRM/", "startid");
    private final static QName _ReserveFlight_QNAME = new QName("http://ws.middleRM/", "reserveFlight");
    private final static QName _AddFlight_QNAME = new QName("http://ws.middleRM/", "addFlight");
    private final static QName _ReserveFlightResponse_QNAME = new QName("http://ws.middleRM/", "reserveFlightResponse");
    private final static QName _NewCustomerId_QNAME = new QName("http://ws.middleRM/", "newCustomerId");
    private final static QName _QueryFlightPriceResponse_QNAME = new QName("http://ws.middleRM/", "queryFlightPriceResponse");
    private final static QName _DeleteCustomerResponse_QNAME = new QName("http://ws.middleRM/", "deleteCustomerResponse");
    private final static QName _ReserveCar_QNAME = new QName("http://ws.middleRM/", "reserveCar");
    private final static QName _StartidResponse_QNAME = new QName("http://ws.middleRM/", "startidResponse");
    private final static QName _QueryCarsPrice_QNAME = new QName("http://ws.middleRM/", "queryCarsPrice");
    private final static QName _AbortResponse_QNAME = new QName("http://ws.middleRM/", "abortResponse");
    private final static QName _StartResponse_QNAME = new QName("http://ws.middleRM/", "startResponse");
    private final static QName _Commit_QNAME = new QName("http://ws.middleRM/", "commit");
    private final static QName _QueryRoomsPriceResponse_QNAME = new QName("http://ws.middleRM/", "queryRoomsPriceResponse");
    private final static QName _Abort_QNAME = new QName("http://ws.middleRM/", "abort");
    private final static QName _DeleteCarsResponse_QNAME = new QName("http://ws.middleRM/", "deleteCarsResponse");
    private final static QName _DeleteFlightResponse_QNAME = new QName("http://ws.middleRM/", "deleteFlightResponse");
    private final static QName _Shutdown_QNAME = new QName("http://ws.middleRM/", "shutdown");
    private final static QName _ShutdownResponse_QNAME = new QName("http://ws.middleRM/", "shutdownResponse");
    private final static QName _QueryRoomsResponse_QNAME = new QName("http://ws.middleRM/", "queryRoomsResponse");
    private final static QName _ReserveItineraryResponse_QNAME = new QName("http://ws.middleRM/", "reserveItineraryResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: client
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link StartidResponse }
     * 
     */
    public StartidResponse createStartidResponse() {
        return new StartidResponse();
    }

    /**
     * Create an instance of {@link QueryCarsPrice }
     * 
     */
    public QueryCarsPrice createQueryCarsPrice() {
        return new QueryCarsPrice();
    }

    /**
     * Create an instance of {@link Commit }
     * 
     */
    public Commit createCommit() {
        return new Commit();
    }

    /**
     * Create an instance of {@link AbortResponse }
     * 
     */
    public AbortResponse createAbortResponse() {
        return new AbortResponse();
    }

    /**
     * Create an instance of {@link StartResponse }
     * 
     */
    public StartResponse createStartResponse() {
        return new StartResponse();
    }

    /**
     * Create an instance of {@link QueryRoomsPriceResponse }
     * 
     */
    public QueryRoomsPriceResponse createQueryRoomsPriceResponse() {
        return new QueryRoomsPriceResponse();
    }

    /**
     * Create an instance of {@link NewCustomerIdResponse }
     * 
     */
    public NewCustomerIdResponse createNewCustomerIdResponse() {
        return new NewCustomerIdResponse();
    }

    /**
     * Create an instance of {@link QueryFlightPrice }
     * 
     */
    public QueryFlightPrice createQueryFlightPrice() {
        return new QueryFlightPrice();
    }

    /**
     * Create an instance of {@link ReserveRoomResponse }
     * 
     */
    public ReserveRoomResponse createReserveRoomResponse() {
        return new ReserveRoomResponse();
    }

    /**
     * Create an instance of {@link AddFlightResponse }
     * 
     */
    public AddFlightResponse createAddFlightResponse() {
        return new AddFlightResponse();
    }

    /**
     * Create an instance of {@link DeleteRoomsResponse }
     * 
     */
    public DeleteRoomsResponse createDeleteRoomsResponse() {
        return new DeleteRoomsResponse();
    }

    /**
     * Create an instance of {@link Startid }
     * 
     */
    public Startid createStartid() {
        return new Startid();
    }

    /**
     * Create an instance of {@link NewCustomer }
     * 
     */
    public NewCustomer createNewCustomer() {
        return new NewCustomer();
    }

    /**
     * Create an instance of {@link AddFlight }
     * 
     */
    public AddFlight createAddFlight() {
        return new AddFlight();
    }

    /**
     * Create an instance of {@link ReserveFlight }
     * 
     */
    public ReserveFlight createReserveFlight() {
        return new ReserveFlight();
    }

    /**
     * Create an instance of {@link ReserveFlightResponse }
     * 
     */
    public ReserveFlightResponse createReserveFlightResponse() {
        return new ReserveFlightResponse();
    }

    /**
     * Create an instance of {@link NewCustomerId }
     * 
     */
    public NewCustomerId createNewCustomerId() {
        return new NewCustomerId();
    }

    /**
     * Create an instance of {@link DeleteCustomerResponse }
     * 
     */
    public DeleteCustomerResponse createDeleteCustomerResponse() {
        return new DeleteCustomerResponse();
    }

    /**
     * Create an instance of {@link ReserveCar }
     * 
     */
    public ReserveCar createReserveCar() {
        return new ReserveCar();
    }

    /**
     * Create an instance of {@link QueryFlightPriceResponse }
     * 
     */
    public QueryFlightPriceResponse createQueryFlightPriceResponse() {
        return new QueryFlightPriceResponse();
    }

    /**
     * Create an instance of {@link ShutdownResponse }
     * 
     */
    public ShutdownResponse createShutdownResponse() {
        return new ShutdownResponse();
    }

    /**
     * Create an instance of {@link QueryRoomsResponse }
     * 
     */
    public QueryRoomsResponse createQueryRoomsResponse() {
        return new QueryRoomsResponse();
    }

    /**
     * Create an instance of {@link ReserveItineraryResponse }
     * 
     */
    public ReserveItineraryResponse createReserveItineraryResponse() {
        return new ReserveItineraryResponse();
    }

    /**
     * Create an instance of {@link Abort }
     * 
     */
    public Abort createAbort() {
        return new Abort();
    }

    /**
     * Create an instance of {@link DeleteFlightResponse }
     * 
     */
    public DeleteFlightResponse createDeleteFlightResponse() {
        return new DeleteFlightResponse();
    }

    /**
     * Create an instance of {@link DeleteCarsResponse }
     * 
     */
    public DeleteCarsResponse createDeleteCarsResponse() {
        return new DeleteCarsResponse();
    }

    /**
     * Create an instance of {@link Shutdown }
     * 
     */
    public Shutdown createShutdown() {
        return new Shutdown();
    }

    /**
     * Create an instance of {@link QueryCars }
     * 
     */
    public QueryCars createQueryCars() {
        return new QueryCars();
    }

    /**
     * Create an instance of {@link ReserveRoom }
     * 
     */
    public ReserveRoom createReserveRoom() {
        return new ReserveRoom();
    }

    /**
     * Create an instance of {@link AddRooms }
     * 
     */
    public AddRooms createAddRooms() {
        return new AddRooms();
    }

    /**
     * Create an instance of {@link QueryRoomsPrice }
     * 
     */
    public QueryRoomsPrice createQueryRoomsPrice() {
        return new QueryRoomsPrice();
    }

    /**
     * Create an instance of {@link QueryCustomerInfo }
     * 
     */
    public QueryCustomerInfo createQueryCustomerInfo() {
        return new QueryCustomerInfo();
    }

    /**
     * Create an instance of {@link DeleteCars }
     * 
     */
    public DeleteCars createDeleteCars() {
        return new DeleteCars();
    }

    /**
     * Create an instance of {@link QueryRooms }
     * 
     */
    public QueryRooms createQueryRooms() {
        return new QueryRooms();
    }

    /**
     * Create an instance of {@link ReserveCarResponse }
     * 
     */
    public ReserveCarResponse createReserveCarResponse() {
        return new ReserveCarResponse();
    }

    /**
     * Create an instance of {@link CommitResponse }
     * 
     */
    public CommitResponse createCommitResponse() {
        return new CommitResponse();
    }

    /**
     * Create an instance of {@link QueryCustomerInfoResponse }
     * 
     */
    public QueryCustomerInfoResponse createQueryCustomerInfoResponse() {
        return new QueryCustomerInfoResponse();
    }

    /**
     * Create an instance of {@link AddCarsResponse }
     * 
     */
    public AddCarsResponse createAddCarsResponse() {
        return new AddCarsResponse();
    }

    /**
     * Create an instance of {@link AddCars }
     * 
     */
    public AddCars createAddCars() {
        return new AddCars();
    }

    /**
     * Create an instance of {@link AddRoomsResponse }
     * 
     */
    public AddRoomsResponse createAddRoomsResponse() {
        return new AddRoomsResponse();
    }

    /**
     * Create an instance of {@link QueryCarsPriceResponse }
     * 
     */
    public QueryCarsPriceResponse createQueryCarsPriceResponse() {
        return new QueryCarsPriceResponse();
    }

    /**
     * Create an instance of {@link ReserveItinerary }
     * 
     */
    public ReserveItinerary createReserveItinerary() {
        return new ReserveItinerary();
    }

    /**
     * Create an instance of {@link DeleteRooms }
     * 
     */
    public DeleteRooms createDeleteRooms() {
        return new DeleteRooms();
    }

    /**
     * Create an instance of {@link NewCustomerResponse }
     * 
     */
    public NewCustomerResponse createNewCustomerResponse() {
        return new NewCustomerResponse();
    }

    /**
     * Create an instance of {@link Start }
     * 
     */
    public Start createStart() {
        return new Start();
    }

    /**
     * Create an instance of {@link QueryFlight }
     * 
     */
    public QueryFlight createQueryFlight() {
        return new QueryFlight();
    }

    /**
     * Create an instance of {@link DeleteCustomer }
     * 
     */
    public DeleteCustomer createDeleteCustomer() {
        return new DeleteCustomer();
    }

    /**
     * Create an instance of {@link DeleteFlight }
     * 
     */
    public DeleteFlight createDeleteFlight() {
        return new DeleteFlight();
    }

    /**
     * Create an instance of {@link QueryCarsResponse }
     * 
     */
    public QueryCarsResponse createQueryCarsResponse() {
        return new QueryCarsResponse();
    }

    /**
     * Create an instance of {@link QueryFlightResponse }
     * 
     */
    public QueryFlightResponse createQueryFlightResponse() {
        return new QueryFlightResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryRooms }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.middleRM/", name = "queryRooms")
    public JAXBElement<QueryRooms> createQueryRooms(QueryRooms value) {
        return new JAXBElement<QueryRooms>(_QueryRooms_QNAME, QueryRooms.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CommitResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.middleRM/", name = "commitResponse")
    public JAXBElement<CommitResponse> createCommitResponse(CommitResponse value) {
        return new JAXBElement<CommitResponse>(_CommitResponse_QNAME, CommitResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReserveCarResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.middleRM/", name = "reserveCarResponse")
    public JAXBElement<ReserveCarResponse> createReserveCarResponse(ReserveCarResponse value) {
        return new JAXBElement<ReserveCarResponse>(_ReserveCarResponse_QNAME, ReserveCarResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryCustomerInfoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.middleRM/", name = "queryCustomerInfoResponse")
    public JAXBElement<QueryCustomerInfoResponse> createQueryCustomerInfoResponse(QueryCustomerInfoResponse value) {
        return new JAXBElement<QueryCustomerInfoResponse>(_QueryCustomerInfoResponse_QNAME, QueryCustomerInfoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddCarsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.middleRM/", name = "addCarsResponse")
    public JAXBElement<AddCarsResponse> createAddCarsResponse(AddCarsResponse value) {
        return new JAXBElement<AddCarsResponse>(_AddCarsResponse_QNAME, AddCarsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryCars }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.middleRM/", name = "queryCars")
    public JAXBElement<QueryCars> createQueryCars(QueryCars value) {
        return new JAXBElement<QueryCars>(_QueryCars_QNAME, QueryCars.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReserveRoom }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.middleRM/", name = "reserveRoom")
    public JAXBElement<ReserveRoom> createReserveRoom(ReserveRoom value) {
        return new JAXBElement<ReserveRoom>(_ReserveRoom_QNAME, ReserveRoom.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryRoomsPrice }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.middleRM/", name = "queryRoomsPrice")
    public JAXBElement<QueryRoomsPrice> createQueryRoomsPrice(QueryRoomsPrice value) {
        return new JAXBElement<QueryRoomsPrice>(_QueryRoomsPrice_QNAME, QueryRoomsPrice.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddRooms }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.middleRM/", name = "addRooms")
    public JAXBElement<AddRooms> createAddRooms(AddRooms value) {
        return new JAXBElement<AddRooms>(_AddRooms_QNAME, AddRooms.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryCustomerInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.middleRM/", name = "queryCustomerInfo")
    public JAXBElement<QueryCustomerInfo> createQueryCustomerInfo(QueryCustomerInfo value) {
        return new JAXBElement<QueryCustomerInfo>(_QueryCustomerInfo_QNAME, QueryCustomerInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteCars }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.middleRM/", name = "deleteCars")
    public JAXBElement<DeleteCars> createDeleteCars(DeleteCars value) {
        return new JAXBElement<DeleteCars>(_DeleteCars_QNAME, DeleteCars.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteFlight }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.middleRM/", name = "deleteFlight")
    public JAXBElement<DeleteFlight> createDeleteFlight(DeleteFlight value) {
        return new JAXBElement<DeleteFlight>(_DeleteFlight_QNAME, DeleteFlight.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryCarsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.middleRM/", name = "queryCarsResponse")
    public JAXBElement<QueryCarsResponse> createQueryCarsResponse(QueryCarsResponse value) {
        return new JAXBElement<QueryCarsResponse>(_QueryCarsResponse_QNAME, QueryCarsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryFlightResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.middleRM/", name = "queryFlightResponse")
    public JAXBElement<QueryFlightResponse> createQueryFlightResponse(QueryFlightResponse value) {
        return new JAXBElement<QueryFlightResponse>(_QueryFlightResponse_QNAME, QueryFlightResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryCarsPriceResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.middleRM/", name = "queryCarsPriceResponse")
    public JAXBElement<QueryCarsPriceResponse> createQueryCarsPriceResponse(QueryCarsPriceResponse value) {
        return new JAXBElement<QueryCarsPriceResponse>(_QueryCarsPriceResponse_QNAME, QueryCarsPriceResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReserveItinerary }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.middleRM/", name = "reserveItinerary")
    public JAXBElement<ReserveItinerary> createReserveItinerary(ReserveItinerary value) {
        return new JAXBElement<ReserveItinerary>(_ReserveItinerary_QNAME, ReserveItinerary.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddCars }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.middleRM/", name = "addCars")
    public JAXBElement<AddCars> createAddCars(AddCars value) {
        return new JAXBElement<AddCars>(_AddCars_QNAME, AddCars.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddRoomsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.middleRM/", name = "addRoomsResponse")
    public JAXBElement<AddRoomsResponse> createAddRoomsResponse(AddRoomsResponse value) {
        return new JAXBElement<AddRoomsResponse>(_AddRoomsResponse_QNAME, AddRoomsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteRooms }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.middleRM/", name = "deleteRooms")
    public JAXBElement<DeleteRooms> createDeleteRooms(DeleteRooms value) {
        return new JAXBElement<DeleteRooms>(_DeleteRooms_QNAME, DeleteRooms.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NewCustomerResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.middleRM/", name = "newCustomerResponse")
    public JAXBElement<NewCustomerResponse> createNewCustomerResponse(NewCustomerResponse value) {
        return new JAXBElement<NewCustomerResponse>(_NewCustomerResponse_QNAME, NewCustomerResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryFlight }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.middleRM/", name = "queryFlight")
    public JAXBElement<QueryFlight> createQueryFlight(QueryFlight value) {
        return new JAXBElement<QueryFlight>(_QueryFlight_QNAME, QueryFlight.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Start }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.middleRM/", name = "start")
    public JAXBElement<Start> createStart(Start value) {
        return new JAXBElement<Start>(_Start_QNAME, Start.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteCustomer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.middleRM/", name = "deleteCustomer")
    public JAXBElement<DeleteCustomer> createDeleteCustomer(DeleteCustomer value) {
        return new JAXBElement<DeleteCustomer>(_DeleteCustomer_QNAME, DeleteCustomer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddFlightResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.middleRM/", name = "addFlightResponse")
    public JAXBElement<AddFlightResponse> createAddFlightResponse(AddFlightResponse value) {
        return new JAXBElement<AddFlightResponse>(_AddFlightResponse_QNAME, AddFlightResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NewCustomerIdResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.middleRM/", name = "newCustomerIdResponse")
    public JAXBElement<NewCustomerIdResponse> createNewCustomerIdResponse(NewCustomerIdResponse value) {
        return new JAXBElement<NewCustomerIdResponse>(_NewCustomerIdResponse_QNAME, NewCustomerIdResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryFlightPrice }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.middleRM/", name = "queryFlightPrice")
    public JAXBElement<QueryFlightPrice> createQueryFlightPrice(QueryFlightPrice value) {
        return new JAXBElement<QueryFlightPrice>(_QueryFlightPrice_QNAME, QueryFlightPrice.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReserveRoomResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.middleRM/", name = "reserveRoomResponse")
    public JAXBElement<ReserveRoomResponse> createReserveRoomResponse(ReserveRoomResponse value) {
        return new JAXBElement<ReserveRoomResponse>(_ReserveRoomResponse_QNAME, ReserveRoomResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteRoomsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.middleRM/", name = "deleteRoomsResponse")
    public JAXBElement<DeleteRoomsResponse> createDeleteRoomsResponse(DeleteRoomsResponse value) {
        return new JAXBElement<DeleteRoomsResponse>(_DeleteRoomsResponse_QNAME, DeleteRoomsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NewCustomer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.middleRM/", name = "newCustomer")
    public JAXBElement<NewCustomer> createNewCustomer(NewCustomer value) {
        return new JAXBElement<NewCustomer>(_NewCustomer_QNAME, NewCustomer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Startid }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.middleRM/", name = "startid")
    public JAXBElement<Startid> createStartid(Startid value) {
        return new JAXBElement<Startid>(_Startid_QNAME, Startid.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReserveFlight }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.middleRM/", name = "reserveFlight")
    public JAXBElement<ReserveFlight> createReserveFlight(ReserveFlight value) {
        return new JAXBElement<ReserveFlight>(_ReserveFlight_QNAME, ReserveFlight.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddFlight }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.middleRM/", name = "addFlight")
    public JAXBElement<AddFlight> createAddFlight(AddFlight value) {
        return new JAXBElement<AddFlight>(_AddFlight_QNAME, AddFlight.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReserveFlightResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.middleRM/", name = "reserveFlightResponse")
    public JAXBElement<ReserveFlightResponse> createReserveFlightResponse(ReserveFlightResponse value) {
        return new JAXBElement<ReserveFlightResponse>(_ReserveFlightResponse_QNAME, ReserveFlightResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NewCustomerId }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.middleRM/", name = "newCustomerId")
    public JAXBElement<NewCustomerId> createNewCustomerId(NewCustomerId value) {
        return new JAXBElement<NewCustomerId>(_NewCustomerId_QNAME, NewCustomerId.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryFlightPriceResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.middleRM/", name = "queryFlightPriceResponse")
    public JAXBElement<QueryFlightPriceResponse> createQueryFlightPriceResponse(QueryFlightPriceResponse value) {
        return new JAXBElement<QueryFlightPriceResponse>(_QueryFlightPriceResponse_QNAME, QueryFlightPriceResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteCustomerResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.middleRM/", name = "deleteCustomerResponse")
    public JAXBElement<DeleteCustomerResponse> createDeleteCustomerResponse(DeleteCustomerResponse value) {
        return new JAXBElement<DeleteCustomerResponse>(_DeleteCustomerResponse_QNAME, DeleteCustomerResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReserveCar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.middleRM/", name = "reserveCar")
    public JAXBElement<ReserveCar> createReserveCar(ReserveCar value) {
        return new JAXBElement<ReserveCar>(_ReserveCar_QNAME, ReserveCar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StartidResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.middleRM/", name = "startidResponse")
    public JAXBElement<StartidResponse> createStartidResponse(StartidResponse value) {
        return new JAXBElement<StartidResponse>(_StartidResponse_QNAME, StartidResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryCarsPrice }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.middleRM/", name = "queryCarsPrice")
    public JAXBElement<QueryCarsPrice> createQueryCarsPrice(QueryCarsPrice value) {
        return new JAXBElement<QueryCarsPrice>(_QueryCarsPrice_QNAME, QueryCarsPrice.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbortResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.middleRM/", name = "abortResponse")
    public JAXBElement<AbortResponse> createAbortResponse(AbortResponse value) {
        return new JAXBElement<AbortResponse>(_AbortResponse_QNAME, AbortResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StartResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.middleRM/", name = "startResponse")
    public JAXBElement<StartResponse> createStartResponse(StartResponse value) {
        return new JAXBElement<StartResponse>(_StartResponse_QNAME, StartResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Commit }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.middleRM/", name = "commit")
    public JAXBElement<Commit> createCommit(Commit value) {
        return new JAXBElement<Commit>(_Commit_QNAME, Commit.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryRoomsPriceResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.middleRM/", name = "queryRoomsPriceResponse")
    public JAXBElement<QueryRoomsPriceResponse> createQueryRoomsPriceResponse(QueryRoomsPriceResponse value) {
        return new JAXBElement<QueryRoomsPriceResponse>(_QueryRoomsPriceResponse_QNAME, QueryRoomsPriceResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Abort }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.middleRM/", name = "abort")
    public JAXBElement<Abort> createAbort(Abort value) {
        return new JAXBElement<Abort>(_Abort_QNAME, Abort.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteCarsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.middleRM/", name = "deleteCarsResponse")
    public JAXBElement<DeleteCarsResponse> createDeleteCarsResponse(DeleteCarsResponse value) {
        return new JAXBElement<DeleteCarsResponse>(_DeleteCarsResponse_QNAME, DeleteCarsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteFlightResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.middleRM/", name = "deleteFlightResponse")
    public JAXBElement<DeleteFlightResponse> createDeleteFlightResponse(DeleteFlightResponse value) {
        return new JAXBElement<DeleteFlightResponse>(_DeleteFlightResponse_QNAME, DeleteFlightResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Shutdown }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.middleRM/", name = "shutdown")
    public JAXBElement<Shutdown> createShutdown(Shutdown value) {
        return new JAXBElement<Shutdown>(_Shutdown_QNAME, Shutdown.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ShutdownResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.middleRM/", name = "shutdownResponse")
    public JAXBElement<ShutdownResponse> createShutdownResponse(ShutdownResponse value) {
        return new JAXBElement<ShutdownResponse>(_ShutdownResponse_QNAME, ShutdownResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryRoomsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.middleRM/", name = "queryRoomsResponse")
    public JAXBElement<QueryRoomsResponse> createQueryRoomsResponse(QueryRoomsResponse value) {
        return new JAXBElement<QueryRoomsResponse>(_QueryRoomsResponse_QNAME, QueryRoomsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReserveItineraryResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.middleRM/", name = "reserveItineraryResponse")
    public JAXBElement<ReserveItineraryResponse> createReserveItineraryResponse(ReserveItineraryResponse value) {
        return new JAXBElement<ReserveItineraryResponse>(_ReserveItineraryResponse_QNAME, ReserveItineraryResponse.class, null, value);
    }

}
