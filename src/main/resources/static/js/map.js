// Replace with your own API key
const API_KEY = 'zjEFy9NsTMuP_e3U9_B0sDu_axPSSl28smWg1PXW4i0';

// Initialize temporary coordinates
let tempCoordinates = null;
let savedMarker = null;
let allRouteDuration = null;
let coordinatesOfTrip = null;
let jsonGeometryWaypoints = [];

// Initialize the map
const map = new maplibregl.Map({
    container: 'map',
    center: [19.0318300, 49.7172400],
    zoom: 7,
    style: {
        version: 8,
        sources: {
            'basic-tiles': {
                type: 'raster',
                url: `https://api.mapy.cz/v1/maptiles/outdoor/tiles.json?apikey=${API_KEY}`,
                tileSize: 256,
            },
            'route-geometry': {
                type: 'geojson',
                data: {
                    type: "LineString",
                    coordinates: [],
                },
            },
        },
        layers: [
            {
                id: 'tiles',
                type: 'raster',
                source: 'basic-tiles',
            },
            {
                id: 'route-geometry',
                type: 'line',
                source: 'route-geometry',
                layout: {
                    'line-join': 'round',
                    'line-cap': 'round',
                },
                paint: {
                    'line-color': '#0033ff',
                    'line-width': 8,
                    'line-opacity': 0.6,
                },
            },
        ],
    },
});

// Store waypoints and POI markers
let waypoints = [];
let poiMarkers = [];

// Add LogoControl
class LogoControl {
    onAdd(map) {
        this._map = map;
        this._container = document.createElement('div');
        this._container.className = 'maplibregl-ctrl';
        this._container.innerHTML = '<a href="http://mapy.cz/" target="_blank"><img width="100px" src="https://api.mapy.cz/img/api/logo.svg"></a>';
        return this._container;
    }

    onRemove() {
        this._container.parentNode.removeChild(this._container);
        this._map = undefined;
    }
}

map.addControl(new LogoControl(), 'bottom-left');

// Function to fetch POI based on current bounding box and selected categories
async function fetchPOI(bbox) {
    const [sw, ne] = bbox;

    // Get selected categories from checkboxes
    const selectedCategories = [];
    if (document.getElementById('restaurant').checked) selectedCategories.push("restaurant");
    if (document.getElementById('cafe').checked) selectedCategories.push("cafe");
    if (document.getElementById('parking').checked) selectedCategories.push("parking");
    if (document.getElementById('school').checked) selectedCategories.push("school");
    if (document.getElementById('hospital').checked) selectedCategories.push("hospital");
    if (document.getElementById('peak').checked) selectedCategories.push("peak");

    const includeGeneralPOI = document.getElementById('generalPoi').checked;

    // If no categories are selected and general POIs are disabled, return early with no POIs
    if (selectedCategories.length === 0 && !includeGeneralPOI) {
        return [];
    }

    // Create Overpass query based on selected categories
    const query = `
    [out:json];
    (
      ${selectedCategories.map(cat => `node["amenity"="${cat}"]["name"](${sw[1]}, ${sw[0]}, ${ne[1]}, ${ne[0]});`).join('')}
      node["shop"]["name"](${sw[1]}, ${sw[0]}, ${ne[1]}, ${ne[0]});  // Shops are always included
      ${selectedCategories.includes('peak') ? `node["natural"="peak"](${sw[1]}, ${sw[0]}, ${ne[1]}, ${ne[0]});` : ''}
    );
    out body;
    `;

    const overpassUrl = 'https://overpass-api.de/api/interpreter?data=' + encodeURIComponent(query);

    try {
        const response = await fetch(overpassUrl);
        const data = await response.json();

        return data.elements.map(poi => {
            const amenity = poi.tags.amenity || poi.tags.shop || poi.tags.natural || "Nieznany";
            const icon = iconMap[amenity] || '📍';

            // If general POIs are disabled, filter out those without specific categories
            if (!includeGeneralPOI && icon === '📍') return null;

            // Check if the POI has a name; if not, exclude it
            if (!poi.tags.name) return null;

            return {
                name: poi.tags.name,
                coords: [poi.lon, poi.lat],
                icon: icon,
            };
        }).filter(poi => poi !== null);  // Filter out null values (POIs without names or excluded)
    } catch (error) {
        console.error("Błąd pobierania danych POI:", error);
        return [];
    }
}


// Function to clear existing POI markers
function clearPOIMarkers() {
    poiMarkers.forEach(marker => marker.remove());
    poiMarkers = [];
}

// Function to create a POI marker element
function createPoiElement(icon) {
    const div = document.createElement('div');
    div.className = 'poi-marker';
    div.textContent = icon;
    return div;
}

// Function to update POI markers based on current map view
async function updatePOIMarkers() {
    const zoomLevel = map.getZoom();

    if (zoomLevel < 13) {
        clearPOIMarkers();
        return;
    }

    const bounds = map.getBounds();
    const bbox = [[bounds.getWest(), bounds.getSouth()], [bounds.getEast(), bounds.getNorth()]];

    // download selected categories
    const selectedCategories = Array.from(document.querySelectorAll('#poiFilters input:checked'))
        .map(input => input.value);

    const poiData = await fetchPOI(bbox, selectedCategories);

    clearPOIMarkers();

    poiData.forEach(poi => {
        const markerElement = createPoiElement(poi.icon);
        const marker = new maplibregl.Marker({element: markerElement})
            .setLngLat(poi.coords)
            .addTo(map);

        const popupContent = document.createElement('div');
        popupContent.innerHTML = `<strong>${poi.name}</strong><br>`;

        const addToRouteBtn = document.createElement('button');
        addToRouteBtn.textContent = 'Add waypoint';
        addToRouteBtn.style.marginTop = '5px';

        addToRouteBtn.addEventListener('click', () => {
            waypoints.push({coords: poi.coords, name: poi.name});
            updateWaypointsList();
            route();
        });

        popupContent.appendChild(addToRouteBtn);

        const popup = new maplibregl.Popup({closeOnClick: false})
            .setDOMContent(popupContent);

        let isMouseOverPopup = false;
        let isMouseOverMarker = false;
        let closePopupTimeout = null;

        function closePopupWithDelay() {
            closePopupTimeout = setTimeout(() => {
                if (!isMouseOverPopup && !isMouseOverMarker) {
                    popup.remove();
                }
            }, 300);
        }

        markerElement.addEventListener('mouseenter', () => {
            if (closePopupTimeout) clearTimeout(closePopupTimeout);
            popup.setLngLat(poi.coords).addTo(map);
            isMouseOverMarker = true;
        });

        markerElement.addEventListener('mouseleave', () => {
            isMouseOverMarker = false;
            closePopupWithDelay();
        });

        popupContent.addEventListener('mouseenter', () => {
            if (closePopupTimeout) clearTimeout(closePopupTimeout);
            isMouseOverPopup = true;
        });

        popupContent.addEventListener('mouseleave', () => {
            isMouseOverPopup = false;
            closePopupWithDelay();
        });

        poiMarkers.push(marker);
    });
}

// Icon mapping
const iconMap = {
    restaurant: '🍽️',
    cafe: '☕',
    parking: '🅿️',
    shop: '🛒',
    school: '🏫',
    hospital: '🏥',
    peak: '⛰️',             // Icon for Peaks
    pharmacy: '💊',       // Apteki
    bank: '🏦',           // Banki
    post_office: '📮',    // Poczty
    fuel: '⛽',           // Stacje benzynowe
    police: '👮',         // Posterunki policji
    fire_station: '🚒',   // Straż pożarna
    library: '📚',        // Biblioteki
    atm: '🏧',            // Bankomaty
    bar: '🍻',            // Bary
    fast_food: '🍔',      // Fast foody
    hotel: '🏨',          // Hotele
    cinema: '🎥',         // Kina
    museum: '🏛️',        // Muzea
    park: '🌳',           // Parki
};


// Listen for map movements and zoom changes to load new POIs
map.on('moveend', updatePOIMarkers);
map.on('zoomend', updatePOIMarkers);

// Function to calculate bounding box from coordinates
function bbox(coords) {
    let minLat = Infinity, minLng = Infinity, maxLat = -Infinity, maxLng = -Infinity;
    coords.forEach(c => {
        minLng = Math.min(c[0], minLng);
        maxLng = Math.max(c[0], maxLng);
        minLat = Math.min(c[1], minLat);
        maxLat = Math.max(c[1], maxLat);
    });
    return [[minLng, minLat], [maxLng, maxLat]];
}

// Function to route between waypoints
async function route() {
    if (waypoints.length < 2) return;

    const url = new URL('https://api.mapy.cz/v1/routing/route');
    url.searchParams.set('apikey', API_KEY);
    url.searchParams.set('lang', 'cs');
    url.searchParams.set('start', waypoints[0].coords.join(','));
    url.searchParams.set('end', waypoints[waypoints.length - 1].coords.join(','));
    url.searchParams.set('routeType', 'foot_fast'); //foot_fast, bike_mountain
    waypoints.slice(1, -1).forEach(wp => url.searchParams.append('waypoints', wp.coords.join(',')));

    const response = await fetch(url.toString(), {mode: 'cors'});
    const json = await response.json();

    coordinatesOfTrip = json;

    if (json.geometry) {
        const source = map.getSource('route-geometry');
        source.setData(json.geometry);

        // console.log(json.geometry);
        console.log(json.geometry.geometry);

        getArraryOfCoordinates(json.geometry);

        document.getElementById('distance').textContent = `${(json.length / 1000).toFixed(2)} km`;

        let durationInSeconds = json.duration;
        let hours = Math.floor(durationInSeconds / 3600);
        let minutes = Math.floor((durationInSeconds % 3600) / 60);
        let seconds = durationInSeconds % 60;

        if (hours > 0) {
            document.getElementById('duration').textContent = `${hours}h ${minutes}m ${seconds}s`;
        } else {
            document.getElementById('duration').textContent = `${minutes}m ${seconds}s`;
        }
        map.fitBounds(bbox(json.geometry.geometry.coordinates), {padding: 40});
        allRouteDuration = `${hours}h ${minutes}m ${seconds}s`;
    }
}

function getArraryOfCoordinates(feature) {
    jsonGeometryWaypoints = []; // reste table
    feature.geometry.coordinates.forEach(coordinate => {
        jsonGeometryWaypoints.push("[" + coordinate + "]");
    });
}

// Add click event to store coordinates
map.on('click', (e) => {
    const coords = [e.lngLat.lng, e.lngLat.lat];
    tempCoordinates = coords;
    console.log(`coordinates saved: ${coords.join(', ')}`);

    // add marker where clicked
    if (savedMarker) {
        savedMarker.remove(); // delete previous marker
    }
    savedMarker = new maplibregl.Marker()
        .setLngLat(coords)
        .addTo(map);

    // Tworzenie popupu z przyciskiem "Add Point"
    const popupContent = document.createElement('div');
    const addButton = document.createElement('button');
    addButton.textContent = 'Add Point';
    addButton.style.marginTop = '5px';

    // add listener to button
    addButton.addEventListener('click', () => {
        waypoints.push({coords: tempCoordinates, name: 'My Point'});
        updateWaypointsList();
        route();  // count route
        console.log("Waypoint added at:", tempCoordinates);
        popup.remove();  // delete popup after add point
    });

    popupContent.appendChild(addButton);

    const popup = new maplibregl.Popup({closeOnClick: false})
        .setLngLat(coords)
        .setDOMContent(popupContent)
        .addTo(map);

    // Obsługa zamykania popupu po 300ms, jeśli myszka wyjedzie poza obszar
    // close poup after 300 ms, if mouseover poupwindow
    let popupTimeout;
    popupContent.addEventListener('mouseleave', () => {
        popupTimeout = setTimeout(() => {
            popup.remove();
        }, 300);
    });

    popupContent.addEventListener('mouseenter', () => {
        clearTimeout(popupTimeout);
    });
});

// Modify the function to update the waypoints list to show names
function updateWaypointsList() {
    const waypointsList = document.getElementById('waypointsList');
    waypointsList.innerHTML = ''; // clear list

    waypoints.forEach((wp, index) => {
        const li = document.createElement('li');
        li.textContent = `${index + 1}: ${wp.name} (${wp.coords.join(', ')})`; // show coordinates

        const deleteBtn = document.createElement('button');
        deleteBtn.textContent = 'delete';
        deleteBtn.addEventListener('click', () => {
            waypoints.splice(index, 1); // delete waypoint
            updateWaypointsList(); // update list
            route(); // count route

            console.log(waypoints.length)
            if (waypoints.length < 2) {
                document.getElementById('distance').textContent = `0 km`;
                document.getElementById('duration').textContent = `0 s`;
                resetRoute();
            }
        });

        li.appendChild(deleteBtn);
        waypointsList.appendChild(li);
    });
}


// add event listener to button who save waypoint
document.getElementById('addSavedWaypointBtn').addEventListener('click', () => {
    if (tempCoordinates) {
        // Dodaj waypoint tak samo jak w przypadku POI
        waypoints.push({coords: tempCoordinates, name: 'My Point'});
        updateWaypointsList();
        route();  // count route
        console.log("Added saved point:", tempCoordinates);

        // add marker to saved waypoint
        const savedMarker = new maplibregl.Marker()
            .setLngLat(tempCoordinates)
            .addTo(map);
    } else {
        console.log("no coordinates tom save.");
    }
});

document.getElementById('searchBtn').addEventListener('click', async () => {
    const searchInput = document.getElementById('searchInput').value;
    if (!searchInput) return; // Jeśli pole wyszukiwania jest puste, wyjdź

    try {
        const url = `https://api.mapy.cz/v1/geocode?query=${searchInput}&lang=pl&limit=5&type=regional&type=poi`;

        const response = await fetch(url, {
            method: 'GET',
            headers: {
                'accept': 'application/json',
                'X-Mapy-Api-Key': API_KEY
            }
        });

        const data = await response.json();
        console.log('Results:', data); // Logowanie wyników w konsoli

        // Wyczyść poprzednie wyniki
        const searchResults = document.getElementById('searchResults');
        searchResults.innerHTML = '';

        // Sprawdź, czy są jakieś wyniki
        if (data.items && data.items.length > 0) {
            searchResults.style.display = 'block'; // Pokaż wyniki

            data.items.forEach((item, index) => {
                const lon = item.position.lon;
                const lat = item.position.lat;
                const label = item.name + "\n" + item.label || `Wynik ${index + 1}`;

                // Stwórz element listy dla każdego wyniku
                const resultItem = document.createElement('div');
                resultItem.textContent = label;
                resultItem.className = 'search-result-item'; // css class

                // Obsługa kliknięcia na wynik
                resultItem.addEventListener('click', () => {
                    waypoints.push({coords: [lon, lat], name: label});
                    updateWaypointsList();
                    route(); // Zaktualizuj trasę

                    // Centrum mapy na wybranym wyniku
                    map.flyTo({
                        center: [lon, lat],
                        zoom: 15
                    });

                    // Dodaj marker na mapie dla wybranego wyniku
                    new maplibregl.Marker()
                        .setLngLat([lon, lat])
                        .setPopup(new maplibregl.Popup().setText(label)) // Etykieta na markerze
                        .addTo(map);

                    // Wyczyść wyniki wyszukiwania po kliknięciu
                    searchResults.innerHTML = '';
                    searchResults.style.display = 'none'; // Ukryj listę
                });

                // Dodaj wynik do listy
                searchResults.appendChild(resultItem);
            });
        } else {
            // Jeśli brak wyników
            const noResults = document.createElement('div');
            noResults.textContent = "Brak wyników.";
            searchResults.appendChild(noResults);
            searchResults.style.display = 'block'; // Pokaż informację o braku wyników
        }
    } catch (error) {
        console.error('Error fetching data:', error);
        const searchResults = document.getElementById('searchResults');
        searchResults.innerHTML = '<div>Wystąpił błąd podczas wyszukiwania.</div>';
        searchResults.style.display = 'block'; // Pokaż błąd
    }
});




document.getElementById('addSavedWaypointBtn').addEventListener('click', () => {
    if (tempCoordinates) {
        waypoints.push({coords: tempCoordinates, name: 'My Point'}); // change nape of point to  "My Point" instead of coordinates
        updateWaypointsList();
        route();
        console.log("added saved point:", tempCoordinates);
    } else {
        console.log("No coordinates to save.");
    }
});

map.on('click', (e) => {
    const coords = [e.lngLat.lng, e.lngLat.lat];
    tempCoordinates = coords;
    console.log(`coordinates saved: ${coords.join(', ')}`);

    // add marker when clicked
    if (savedMarker) {
        savedMarker.remove(); // delete provious marker
    }
    savedMarker = new maplibregl.Marker()
        .setLngLat(coords)
        .addTo(map);
});

// localiye user
function locateUser() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition((position) => {
            const userCoords = [position.coords.longitude, position.coords.latitude];

            // add marker in user place
            const userMarker = new maplibregl.Marker({color: 'red'})
                .setLngLat(userCoords)
                .setPopup(new maplibregl.Popup().setText("Yours localization"))
                .addTo(map);

            map.flyTo({
                center: userCoords,
                zoom: 14
            });

            console.log(`User localized:: ${userCoords.join(', ')}`);
        }, (error) => {
            console.error("localization error: ", error.message);
            alert("can't localize user.");
        });
    } else {
        alert("Your browser does not support geolocation.");
    }
}

// add listener to localize user
document.getElementById('locateMeBtn').addEventListener('click', locateUser);

// get checkbox and display field
const closedGroupCheckbox = document.getElementById('closedGroupCheckbox');
const peopleInputLabel = document.getElementById('peopleInputLabel');
const peopleInput = document.getElementById('peopleInput');

// add listener to changes of checkbox
closedGroupCheckbox.addEventListener('change', function () {
    if (closedGroupCheckbox.checked) {
        peopleInputLabel.style.display = 'inline';
    } else {
        peopleInputLabel.style.display = 'none';
    }
});

const driver = document.getElementById('driver');
const DriverPeopleInputLabel = document.getElementById('DriverPeopleInputLabel');
const DriverPeopleInput = document.getElementById('DriverPeopleInput');

driver.addEventListener('change', function () {
    if (driver.checked) {
        DriverPeopleInputLabel.style.display = 'inline';
    } else {
        DriverPeopleInputLabel.style.display = 'none';
        DriverPeopleInput.value = '';
    }
});

// validate input field - no alphabet only digits
DriverPeopleInput.addEventListener('input', function () {
    const value = DriverPeopleInput.value;

    if (isNaN(value) || value.trim() === "") {
        alert("Please enter a valid number.");
        DriverPeopleInput.value = '';
    } else {
        const numericValue = parseInt(value, 10);
        if (numericValue < 1 || numericValue > 99) {
            alert("Please enter a number from 1 to 99.");
            DriverPeopleInput.value = '';
        }
    }
});

peopleInput.addEventListener('input', function () {
    const value = peopleInput.value;

    if (isNaN(value) || value.trim() === "") {
        alert("Please enter a valid number.");
        peopleInput.value = '';
    } else {
        const numericValue = parseInt(value, 10);
        if (numericValue < 1 || numericValue > 99) {
            alert("Please enter a number from 1 to 99.");
            peopleInput.value = '';
        }
    }
});


///////////////////////////////////////////////////////////////////////
var activeSendButton = false;

var descriptionInput = document.getElementById('description');
var descriptionBool = false;

descriptionInput.addEventListener('input', function () {
    if (descriptionInput.value !== '') {
        document.getElementById('description').style.backgroundColor = 'green';
        descriptionBool = true;
    } else {
        document.getElementById('description').style.backgroundColor = '#c0392b';
        descriptionBool = false;
    }
    checkInputs();
});

var destinationInput = document.getElementById('dest');
var destinationBool = false;

destinationInput.addEventListener('input', function () {
    if (destinationInput.value !== '') {
        document.getElementById('destination-div').style.backgroundColor = "green";
        destinationBool = true;
    } else {
        document.getElementById('destination-div').style.backgroundColor = "#c0392b";
        destinationBool = false;
    }
    checkInputs();
});

var timeInput = document.getElementById('time');
var timeBool = false;

timeInput.addEventListener('input', function () {
    if (timeInput.value !== '') {
        document.getElementById('time').style.backgroundColor = "green";
        timeBool = true;
    } else {
        document.getElementById('time').style.backgroundColor = "#c0392b";
        timeBool = false;
    }
    checkInputs();
});

var submitButton = document.getElementById('add_announcement');

// Funkcja do sprawdzania, czy wszystkie pola są wypełnione
function checkInputs() {

    if (descriptionBool && destinationBool && timeBool) {
        activeSendButton = true;
        submitButton.style.backgroundColor = "green";
        submitButton.disabled = false;
    } else {
        activeSendButton = false;
        submitButton.style.backgroundColor = "#ccc";
        submitButton.disabled = true;
    }
}

function sendToJava() {

    if(activeSendButton) {

        var waypoints1 = waypoints;
        var waypointsLength = waypoints.length;
        var coords2 = coordinatesOfTrip;
        var duration3 = allRouteDuration;
        var description = document.getElementById('description').value;
        const isCheckedDriver = document.getElementById('driver').checked;
        var amountOfPeopleDriver = document.getElementById('DriverPeopleInput').value;
        const isCheckedAnimals = document.getElementById('animals').checked;
        const isCheckedGroup = document.getElementById('closedGroupCheckbox').checked;
        var amountOfPeopleInGroup = document.getElementById('peopleInput').value;
        var dest = document.getElementById('dest').value;
        var date = document.getElementById('dateTime').value;
        var textContentOfDistance = document.getElementById('distance').textContent;

        // Ustawianie wartości w polach ukrytych formularza
        document.getElementById('waypoints').value = JSON.stringify(waypoints1);
        document.getElementById('coordinatesOfTrip').value = JSON.stringify(coords2);
        document.getElementById('allRouteDuration').value = duration3;

        document.getElementById('descriptionOfTrip').value = description;
        document.getElementById('driverCheck').value = isCheckedDriver;
        document.getElementById('amountOfPeopleDriver').value = amountOfPeopleDriver;
        document.getElementById('isCheckedAnimals').bool = isCheckedAnimals;
        document.getElementById('isCheckedGroup').value = isCheckedGroup;
        document.getElementById('amountOfPeopleInGroup').value = amountOfPeopleInGroup;
        document.getElementById('destination').value = dest;
        document.getElementById('date').value = date;
        document.getElementById('distanceOfTrip').value = textContentOfDistance;
        document.getElementById('jsonGeometryWaypoints').value = jsonGeometryWaypoints;
        document.getElementById('waypointsLength').value = waypoints.length;

        if (date.length < 1 || dest.length < 1 || description.length < 1) {
            alert("fulfill description, destination, date and time of trip!")
        } else {
            console.log("wysyłam")
            document.getElementById('saveForm').submit();
        }
    }
}