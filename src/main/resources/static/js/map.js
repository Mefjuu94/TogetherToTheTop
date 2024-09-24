// Replace with your own API key
const API_KEY = 'zjEFy9NsTMuP_e3U9_B0sDu_axPSSl28smWg1PXW4i0';

// Initialize temporary coordinates
let tempCoordinates = null;
let savedMarker = null;

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
        const marker = new maplibregl.Marker({ element: markerElement })
            .setLngLat(poi.coords)
            .addTo(map);

        const popupContent = document.createElement('div');
        popupContent.innerHTML = `<strong>${poi.name}</strong><br>`;

        const addToRouteBtn = document.createElement('button');
        addToRouteBtn.textContent = 'Add waypoint';
        addToRouteBtn.style.marginTop = '5px';

        addToRouteBtn.addEventListener('click', () => {
            waypoints.push({ coords: poi.coords, name: poi.name });
            updateWaypointsList();
            route();
        });

        popupContent.appendChild(addToRouteBtn);

        const popup = new maplibregl.Popup({ closeOnClick: false })
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
    url.searchParams.set('routeType', 'foot_fast');
    waypoints.slice(1, -1).forEach(wp => url.searchParams.append('waypoints', wp.coords.join(',')));

    const response = await fetch(url.toString(), { mode: 'cors' });
    const json = await response.json();

    if (json.geometry) {
        const source = map.getSource('route-geometry');
        source.setData(json.geometry);
        document.getElementById('distance').textContent = `${(json.length / 1000).toFixed(2)} km`;
        document.getElementById('duration').textContent = `${Math.floor((json.duration / 60)/2)}m ${json.duration % 60}s`;
        map.fitBounds(bbox(json.geometry.geometry.coordinates), { padding: 40 });
    }
}

// Add click event to store coordinates //todo
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
        waypoints.push({ coords: tempCoordinates, name: 'My Point' });
        updateWaypointsList();
        route();  // count route
        console.log("Waypoint added at:", tempCoordinates);
        popup.remove();  // delete popup after add point
    });

    popupContent.appendChild(addButton);

    const popup = new maplibregl.Popup({ closeOnClick: false })
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
        li.textContent = `Waypoint ${index + 1}: ${wp.name} (${wp.coords.join(', ')})`; // show coordinates

        const deleteBtn = document.createElement('button');
        deleteBtn.textContent = 'Delete';
        deleteBtn.addEventListener('click', () => {
            waypoints.splice(index, 1); // delete waypoint
            updateWaypointsList(); // update list
            route(); // count route
        });

        li.appendChild(deleteBtn);
        waypointsList.appendChild(li);
    });
}


// add event listener to button who save waypoint
document.getElementById('addSavedWaypointBtn').addEventListener('click', () => {
    if (tempCoordinates) {
        // Dodaj waypoint tak samo jak w przypadku POI
        waypoints.push({ coords: tempCoordinates, name: 'My Point' });
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




// Add event listener to button to add this POI as a waypoint
addToRouteBtn.addEventListener('click', () => {
    if (poi.name) {
        waypoints.push({ coords: poi.coords, name: poi.name || 'Nieznany' });
        updateWaypointsList();  // update list of route points
        route();
        console.log(`Point added ${poi.name} (${poi.coords.join(', ')}) to route`);
    } else {
        console.log("No name for this point.");
    }
});


document.getElementById('searchBtn').addEventListener('click', async () => {
    console.log('click');
    const searchInput = document.getElementById('searchInput').value;
    if (!searchInput) return;

    // Clear existing waypoints if needed
    waypoints = [];
    updateWaypointsList();

    try {
        // Prepare the URL for the Mapy.cz geocoding API
        const response = await fetch(`https://api.mapy.cz/v1/geocoding/search?apikey=${API_KEY}&query=${encodeURIComponent(searchInput)}`);

        // Sprawdź, czy odpowiedź jest poprawna
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }

        const data = await response.json();

        if (data.length > 0) {
            const { coords } = data[0]; // Assuming the first result is the most relevant
            const lon = coords[0];
            const lat = coords[1];

            // Add the searched location as a waypoint
            waypoints.push({ coords: [lon, lat], name: searchInput });
            updateWaypointsList();
            route();

            // Center the map on the searched location
            map.flyTo({
                center: [lon, lat],
                zoom: 15
            });
        } else {
            console.log("No results found.");
        }
    } catch (error) {
        console.error('Error fetching data:', error);
    }
});

document.getElementById('addSavedWaypointBtn').addEventListener('click', () => {
    if (tempCoordinates) {
        waypoints.push({ coords: tempCoordinates, name: 'My Point' }); // change nape of point to  "My Point" instead of coordinates
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


// todo naprawić wyszukiwanie






