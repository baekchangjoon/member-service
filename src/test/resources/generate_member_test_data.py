import mysql.connector
from mysql.connector import Error
from faker import Faker
import random
from datetime import datetime, timedelta

# Faker 인스턴스 생성 (한국어 설정)
fake = Faker('ko_KR')

def create_connection():
    try:
        connection = mysql.connector.connect(
            host='127.0.0.1',
            user='root',
            password='1234',
            database='memberdb'
        )
        if connection.is_connected():
            print("MySQL 데이터베이스에 연결되었습니다.")
            return connection
    except Error as e:
        print(f"Error: {e}")
        return None

def cleanup_data(connection):
    cursor = connection.cursor()
    cursor.execute("SHOW TABLES")
    tables = cursor.fetchall()
    
    for table in tables:
        table_name = table[0]
        try:
            cursor.execute(f"TRUNCATE TABLE {table_name}")
            print(f"{table_name} 테이블의 데이터가 삭제되었습니다.")
        except Error as e:
            print(f"Error truncating {table_name}: {e}")
    
    connection.commit()
    cursor.close()

def generate_member_data(count=100):
    data = []
    for _ in range(count):
        age = random.randint(15, 80)
        row = {
            'member_name': fake.name(),
            'age': age,
            'email': fake.email(),
            'phone_number': fake.phone_number(),
            'is_adult': age >= 19,
            'status': random.choice(['ACTIVE', 'INACTIVE', 'SUSPENDED']),
            'created_at': fake.date_time_between(start_date='-1y', end_date='now'),
            'updated_at': fake.date_time_between(start_date='-1y', end_date='now')
        }
        data.append(row)
    return data

def generate_member_address_data(member_ids, count_per_member=2):
    data = []
    address_types = ['HOME', 'OFFICE']
    cities = ['서울', '부산', '인천', '대구', '대전', '광주', '울산', '세종', '경기', '강원', '충북', '충남', '전북', '전남', '경북', '경남', '제주']
    
    for member_id in member_ids:
        for _ in range(count_per_member):
            address_type = random.choice(address_types)
            city = random.choice(cities)
            row = {
                'member_id': member_id,
                'address_type': address_type,
                'address_line1': fake.street_address(),
                'address_line2': f"{random.randint(1, 999)}동 {random.randint(1, 999)}호",
                'city': city,
                'zipcode': fake.postcode()
            }
            data.append(row)
    return data

def generate_member_membership_data(member_ids):
    data = []
    membership_levels = ['SILVER', 'GOLD', 'PLATINUM']
    
    for member_id in member_ids:
        row = {
            'member_id': member_id,
            'membership_level': random.choice(membership_levels),
            'points': random.randint(0, 10000)
        }
        data.append(row)
    return data

def insert_test_data(connection, table_name, data):
    cursor = connection.cursor()
    
    for row in data:
        columns = ', '.join(row.keys())
        placeholders = ', '.join(['%s'] * len(row))
        query = f"INSERT INTO {table_name} ({columns}) VALUES ({placeholders})"
        
        try:
            cursor.execute(query, list(row.values()))
        except Error as e:
            print(f"Error inserting data into {table_name}: {e}")
            print(f"Query: {query}")
            print(f"Values: {list(row.values())}")
    
    connection.commit()
    cursor.close()

def main():
    connection = create_connection()
    if connection is None:
        return
    
    try:
        # 기존 데이터 삭제
        print("기존 데이터 삭제 중...")
        cleanup_data(connection)
        
        # 각 테이블에 대해 테스트 데이터 생성 및 삽입
        print("\n새로운 테스트 데이터 생성 중...")
        
        # members 테이블 데이터 생성 및 삽입
        print("\nmembers 테이블에 테스트 데이터 생성 중...")
        member_data = generate_member_data()
        insert_test_data(connection, 'members', member_data)
        print(f"members 테이블에 {len(member_data)}개의 테스트 데이터가 삽입되었습니다.")
        
        # 생성된 member_id 목록 가져오기
        cursor = connection.cursor()
        cursor.execute("SELECT id FROM members")
        member_ids = [row[0] for row in cursor.fetchall()]
        cursor.close()
        
        # member_addresses 테이블 데이터 생성 및 삽입
        print("\nmember_addresses 테이블에 테스트 데이터 생성 중...")
        address_data = generate_member_address_data(member_ids)
        insert_test_data(connection, 'member_addresses', address_data)
        print(f"member_addresses 테이블에 {len(address_data)}개의 테스트 데이터가 삽입되었습니다.")
        
        # member_memberships 테이블 데이터 생성 및 삽입
        print("\nmember_memberships 테이블에 테스트 데이터 생성 중...")
        membership_data = generate_member_membership_data(member_ids)
        insert_test_data(connection, 'member_memberships', membership_data)
        print(f"member_memberships 테이블에 {len(membership_data)}개의 테스트 데이터가 삽입되었습니다.")
    
    except Error as e:
        print(f"Error: {e}")
    finally:
        if connection.is_connected():
            connection.close()
            print("\nMySQL 연결이 종료되었습니다.")

if __name__ == "__main__":
    main() 