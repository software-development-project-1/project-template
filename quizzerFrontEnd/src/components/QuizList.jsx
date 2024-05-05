import { useState, useEffect, useRef } from "react";
import { AgGridReact } from "ag-grid-react";
import "ag-grid-community/styles/ag-grid.css";
import "ag-grid-community/styles/ag-theme-material.css";
import { format } from "date-fns";
import { Link } from "react-router-dom";
import './styles.css'


function QuizList() {
  const [quizList, setQuizList] = useState([]);
  const [categories, setCategories] = useState([]);
  const [selectedCategory, setSelectedCategory] = useState("");
  const gridRef = useRef(null);

  const columns = [
    {
      headerName: "Name",
      field: "quizName",
      width: 200,
      cellRenderer: (params) => {
        const link = `/quiz/${params.data.id}`;
        console.log("Link:", link); // Debugging
        return <Link to={link}>{params.value}</Link>;
      },
    },
    {
      headerName: "Description",
      field: "quizDescription",
      width: 300,
    },
    {
      headerName: "Category",
      valueGetter: (params) =>
        params.data.category ? params.data.category.name : "", // Value getter for nested property
      width: 250,
      field: 'categoryName',
      cellRenderer: (params) => {
        return <span style={{ backgroundColor: "rgb(191, 188, 188)", borderRadius: "4px", padding: "2px 4px" }}>{params.value}</span>;
      }
    },
    {
      headerName: "Added on",
      field: "createdAt",
      valueFormatter: (params) => {
        return format(new Date(params.value), "dd.MM.yyyy");
      },
      width: 250,
    },
    {
      headerName: "Published",
      field: "publishedDisplay",
      width: 150,
    },
    {
      headerName: "Results",
      width: 250,
      cellRenderer: (params) => {
        const link = `/quiz/${params.data.id}/answers`;
        return <Link to={link}>See results</Link>;
      },
    },
  ];

  useEffect(() => {
    fetchCategories();
    fetchQuizList(selectedCategory);
  }, [selectedCategory]);

  const fetchQuizList = async (selectedCategory) => {
    try {
      const response = await fetch(
        `${import.meta.env.VITE_BACKEND_URL}/api/QuizApp/quizes?published=true&categoryId=${selectedCategory}`
      );
      if (!response.ok) {
        throw new Error("Error in retrieving quizes " + response.statusText);
      }
      const data = await response.json();
      console.log(data);
      setQuizList(data);
    } catch (error) {
      console.error(error);
    }
  };

  const fetchCategories = async () => {
    try {
      const response = await fetch(
        `${import.meta.env.VITE_BACKEND_URL}/api/QuizApp/categories`
      );
      if (!response.ok) {
        throw new Error(
          "Error in retrieving categories " + response.statusText
        );
      }
      const data = await response.json();
      setCategories(data);
    } catch (error) {
      console.error(error);
    }
  };

  const handleCategoryChange = (event) => {
    setSelectedCategory(event.target.value);
  };

  return (
    <>
      <div>
        <select className="select-style" value={selectedCategory} onChange={handleCategoryChange}>
          <option value="">All Categories</option>
          {categories.map((category) => (
            <option key={category.id} value={category.id}>
              {category.name}
            </option>
          ))}
        </select>
      </div>
      <div className="ag-theme-material">
        <AgGridReact
          domLayout="autoHeight"
          ref={gridRef}
          rowData={quizList}
          columnDefs={columns}
          rowSelection="single"
          onGridReady={(params) => {
            gridRef.current = params.api;
          }}
        />
      </div>
    </>
  );
}

export default QuizList;
